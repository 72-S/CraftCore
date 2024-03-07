package org.craftcore.craftcore.core;
import net.fabricmc.loader.api.FabricLoader;
import org.craftcore.craftcore.CraftCore;

public class ShematicManager {
    public static final String SCHEMATIC_FOLDER_NAME = "schematics";

    public static void initializeShematicsFolder() {
        // Create schematics folder if it doesn't exist
        if (!FabricLoader.getInstance().getGameDir().resolve(SCHEMATIC_FOLDER_NAME).toFile().exists()) {
            FabricLoader.getInstance().getGameDir().resolve(SCHEMATIC_FOLDER_NAME).toFile().mkdir();
            CraftCore.LOGGER.info("Schematics folder created");
        } else {
            CraftCore.LOGGER.info("Schematics folder already exists");
        }
    }

}
