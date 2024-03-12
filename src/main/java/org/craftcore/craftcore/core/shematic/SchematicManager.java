package org.craftcore.craftcore.core.shematic;

import net.fabricmc.loader.api.FabricLoader;
import net.sandrohc.schematic4j.schematic.Schematic;
import org.craftcore.craftcore.CraftCore;

import java.util.HashMap;
import java.util.Map;

public class SchematicManager {
  public static final String SCHEMATIC_FOLDER_NAME = "schematics";
  private static final Map<String, Schematic> loadedSchematics = new HashMap<>();

  public static void addSchematic(String name, Schematic schematic) {
    loadedSchematics.put(name, schematic);
  }

  public static Schematic getSchematic(String name) {
    return loadedSchematics.get(name);
  }
  public static Map<String, Schematic> getLoadedSchematics() {
    return loadedSchematics;
  }

  public static void removeSchematic(String name) {
    loadedSchematics.remove(name);
  }

  public static void initializeSchematicsFolder() {
    if (!FabricLoader.getInstance().getGameDir().resolve(SCHEMATIC_FOLDER_NAME).toFile().exists()) {
      FabricLoader.getInstance().getGameDir().resolve(SCHEMATIC_FOLDER_NAME).toFile().mkdir();
      CraftCore.LOGGER.info("Schematics folder created");
    } else {
      CraftCore.LOGGER.info("Schematics folder already exists");
    }
  }


}
