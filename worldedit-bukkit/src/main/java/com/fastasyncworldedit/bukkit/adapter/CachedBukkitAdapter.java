package com.fastasyncworldedit.bukkit.adapter;

import com.sk89q.worldedit.registry.state.Property;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.world.block.BlockTypesCache;
import com.sk89q.worldedit.world.item.ItemType;
import com.sk89q.worldedit.world.item.ItemTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class CachedBukkitAdapter implements IBukkitAdapter {

    private int[] itemTypes;
    private int[] blockTypes;

    private boolean init() {
        if (itemTypes == null) {
            Material[] materials = Material.values();
            itemTypes = new int[materials.length];
            blockTypes = new int[materials.length];
            for (int i = 0; i < materials.length; i++) {
                Material material = materials[i];
                if (material.isLegacy()) {
                    continue;
                }
                NamespacedKey key = material.getKey();
                String id = key.getNamespace() + ":" + key.getKey();
                if (material.isBlock()) {
                    blockTypes[i] = BlockTypes.get(id).getInternalId();
                }
                if (material.isItem()) {
                    itemTypes[i] = ItemTypes.get(id).getInternalId();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Converts a Material to a ItemType.
     *
     * @param material The material
     * @return The itemtype
     */
    @Override
    public ItemType asItemType(Material material) {
        if (material == null || material.isLegacy()) {
            return null;
        }

        try {
            if (material.ordinal() >= itemTypes.length) {
                synchronized (this) {
                    if (material.ordinal() >= itemTypes.length) {
                        reinitializeArrays();
                    }
                }
            }

            int internalId = itemTypes[material.ordinal()];
            if (internalId == 0 && material.ordinal() != 0) {
                ItemType resolved = resolveItemType(material);
                return resolved != null ? resolved : getDefaultItemType();
            }

            ItemType result = ItemTypes.get(internalId);
            return result != null ? result : getDefaultItemType();
        } catch (NullPointerException e) {
            if (init()) {
                return asItemType(material);
            }
            ItemType resolved = resolveItemType(material);
            return resolved != null ? resolved : getDefaultItemType();
        } catch (ArrayIndexOutOfBoundsException e) {
            synchronized (this) {
                reinitializeArrays();
                if (material.ordinal() >= itemTypes.length) {
                    ItemType resolved = resolveItemType(material);
                    return resolved != null ? resolved : getDefaultItemType();
                }
            }
            ItemType result = ItemTypes.get(itemTypes[material.ordinal()]);
            return result != null ? result : getDefaultItemType();
        }
    }

    private ItemType getDefaultItemType() {
        return ItemTypes.get("minecraft:air");
    }

    @Override
    public BlockType asBlockType(Material material) {
        try {
            if (material.ordinal() >= blockTypes.length) {
                synchronized (this) {
                    if (material.ordinal() >= blockTypes.length) {
                        reinitializeArrays();
                    }
                }
            }

            int internalId = blockTypes[material.ordinal()];
            if (internalId == 0 && material.ordinal() != 0) {
                return resolveBlockType(material);
            }

            return BlockTypesCache.values[internalId];
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            if (init()) {
                return asBlockType(material);
            }
            return resolveBlockType(material);
        }
    }

    private ItemType resolveItemType(Material material) {
        if (material == null || material.isLegacy()) {
            return null;
        }
        NamespacedKey key = material.getKey();
        String id = key.getNamespace() + ":" + key.getKey();
        return ItemTypes.get(id);
    }

    private BlockType resolveBlockType(Material material) {
        if (material == null || material.isLegacy() || !material.isBlock()) {
            return null;
        }
        NamespacedKey key = material.getKey();
        String id = key.getNamespace() + ":" + key.getKey();
        return BlockTypes.get(id);
    }

    private void reinitializeArrays() {
        itemTypes = null;
        blockTypes = null;
        init();
    }

    /**
     * Create a WorldEdit BlockStateHolder from a Bukkit BlockData.
     *
     * @param blockData The Bukkit BlockData
     * @return The WorldEdit BlockState
     */
    @Override
    public BlockState adapt(BlockData blockData) {
        try {
            checkNotNull(blockData);
            Material material = blockData.getMaterial();
            if (material.ordinal() >= blockTypes.length) {
                synchronized (this) {
                    if (material.ordinal() >= blockTypes.length) {
                        reinitializeArrays();
                    }
                }
            }
            BlockType type = BlockTypes.getFromStateId(blockTypes[material.ordinal()]);
            List<? extends Property<?>> propList = type.getProperties();
            if (propList.size() == 0) {
                return type.getDefaultState();
            }
            String properties = blockData.getAsString();
            return BlockState.get(type, properties, type.getDefaultState());
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            if (init()) {
                return adapt(blockData);
            }
            throw e;
        }
    }

    protected abstract int[] getIbdToOrdinal();

    protected abstract int[] getOrdinalToIbdID();

}
