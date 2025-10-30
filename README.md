# FastAsyncWorldEdit - Youer Patched Version

## Overview

This is a modified version of FastAsyncWorldEdit that addresses critical issues with modded item and block type resolution, providing better compatibility with modded Minecraft environments.

## Key Improvements

### 🔧 Enhanced Modded Item/Block Support
- **Fixed modded item type resolution**: Improved handling of items from various mods
- **Enhanced block type recognition**: Better support for custom blocks from mod packs
- **Namespace handling**: Improved parsing of modded item/block namespaces
- **Compatibility improvements**: Better integration with popular mod frameworks

## Technical Details

The patches focus on:
- Item type parsing and validation
- Block state resolution for modded blocks
- Namespace handling improvements
- Registry lookup optimizations

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
