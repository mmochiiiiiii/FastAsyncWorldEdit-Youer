## Overview

A patched version of FastAsyncWorldEdit that extends full support for modded Minecraft environments, including modded items, blocks, fluids, and NBT data preservation.

## Key Improvements

### 🔧 Core Fixes
- **Modded item/block support**: Full compatibility with modded items and blocks, including proper namespace parsing and registry lookup
- **Modded fluid blocks**: Complete support for custom fluid blocks from mods
- **NBT data preservation**: Maintains NBT data during operations, preserving inventories and tile entity data in modded blocks
- **Error handling**: Enhanced validation and error messages for edge cases

## Building from Source

```bash
git clone [repository-url]
cd FastAsyncWorldEdit-Patched
./gradlew build
```

## Contributing

If you encounter issues with specific mods or have suggestions for improvements:
1. Create an issue with mod details and error logs
2. Test with minimal mod setups when possible
3. Include WorldEdit version and server type

## License

This project maintains the same license as the original FastAsyncWorldEdit project.

## Disclaimer

This is an unofficial patch. While we strive for stability, please backup your worlds before using on production servers.

## Credits

* Original FastAsyncWorldEdit developers
