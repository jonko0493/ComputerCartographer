# Computer Cartographer
This Minecraft mod adds a peripheral to ComputerCraft Tweaked that allows for interfacing with common mapping plugins (BlueMap, Dynmap, Pl3xMap, and Squaremap).

## Details
Using the peripheral, you can dynamically draw POI (including custom icons), lines, circles, rectangles, and arbitrary polygon area markers to the mapping plugin of your choice.

## Lua Functions
The peripheral has the following Lua functions:

| Function | Parameters | Returns | Description | Example |
|----------|-------------|---------|
| `getAvailableIntegrations` | None | `String[]` | Returns the currently available integrations (the mapping plugins you currently have enabled) | `cart.getAvailableIntegrations()` |
| `refreshIntegrations` | None | `void` | Refreshes integrations manually in case new ones have come online since you placed the computerized cartographer | `cart.refreshIntegrations()` |
| `setCurrentIntegration` | `String integrationName` | `boolean success` | If there is only one integration, it is selected by default. However, if there are multiple integrations, you will need to select one using this command. | `cart.setCurrentIntegration("bluemap")` |
| `getAvailableMaps` | None | `String[]` | Returns an array of the currently available maps you can select for creating markers on | `cart.getAvailableMaps()` |
| `getCurrentMap` | None | `String` | Returns the name of the currently selected map. Note that the name of this map will vary between integrations (e.g. `minecraft:the_nether` in Pl3xMap is `DIM-1` in Dynmap) | `cart.getCurrentMap()` |
| `setCurrentMap` | `String mapName` | `boolean success` | Sets the map to add markers to | `cart.setCurrentMap("minecraft:overworld")` |
| `addMarkerSet` | Adds a new marker set