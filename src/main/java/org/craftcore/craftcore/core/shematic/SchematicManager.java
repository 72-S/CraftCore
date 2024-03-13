package org.craftcore.craftcore.core.shematic;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Position;
import org.craftcore.craftcore.CraftCore;
import org.craftcore.craftcore.core.Position.PositionToStringParser;
import org.craftcore.craftcore.core.Position.StringToPositionParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SchematicManager {
  public static final String SCHEMATIC_FOLDER_NAME = "schematics";
  private static final String ID_FILE_NAME = "schematicIds.txt";
  private static final String idFile =
      FabricLoader.getInstance()
          .getGameDir()
          .resolve(SCHEMATIC_FOLDER_NAME)
          .resolve(ID_FILE_NAME)
          .toString();
  public static final Map<UUID, SchematicInfo> schematicInfos = new HashMap<>();

  public static class SchematicInfo {
    public final String customName;
    public final Position position;
    public final String name;
    public final String fullFileName;
    public final UUID id;
    public final Boolean type;

    public SchematicInfo(
        String customName, Position position, String name, String fullFileName, UUID id, Boolean type) {
      this.customName = customName;
      this.position = position;
      this.name = name;
      this.fullFileName = fullFileName;
      this.id = id;
      this.type = type;
    }
  }

  public static void initializeSchematicsFolder() {
    if (!FabricLoader.getInstance().getGameDir().resolve(SCHEMATIC_FOLDER_NAME).toFile().exists()) {
      FabricLoader.getInstance().getGameDir().resolve(SCHEMATIC_FOLDER_NAME).toFile().mkdir();
      CraftCore.LOGGER.info("Schematics folder created");
    } else {
      CraftCore.LOGGER.info("Schematics folder already exists");
    }
    if (!FabricLoader.getInstance()
        .getGameDir()
        .resolve(SCHEMATIC_FOLDER_NAME)
        .resolve(ID_FILE_NAME)
        .toFile()
        .exists()) {
      try {
        FabricLoader.getInstance()
            .getGameDir()
            .resolve(SCHEMATIC_FOLDER_NAME)
            .resolve(ID_FILE_NAME)
            .toFile()
            .createNewFile();
        CraftCore.LOGGER.info("Schematic ids file created");

      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      CraftCore.LOGGER.info("Schematic ids file already exists");
    }
    loadToMap();
  }

  public static void saveSchematicId(Position playerPosition, String schematicName, String fullFileName, String CustomName, Boolean Type) {
    UUID id = UUID.randomUUID();
    String pos = playerPosition.toString();
    schematicInfos.put(
        id, new SchematicInfo(CustomName, playerPosition, schematicName, fullFileName, id, Type));
    try {
      FileWriter fileWriter = new FileWriter(idFile, true);
      fileWriter.write(
          CustomName + ";" + pos + ";" + schematicName + ";" + fullFileName + ";" + id + ";" + Type + "\n");
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static SchematicInfo getSchematicInfo(UUID schematicId) {
    return schematicInfos.get(schematicId);
  }

private static void loadToMap() {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(idFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length < 6) {
                CraftCore.LOGGER.error("Invalid line in schematic IDs file: " + line);
                continue;
            }
            String customName = parts[0];
            Position position = StringToPositionParser.parsePosition(parts[1]);
            String schematicName = parts[2];
            String fullFileName = parts[3];
            UUID id = UUID.fromString(parts[4]);
            Boolean type = Boolean.parseBoolean(parts[5]);

            schematicInfos.put(
                id, new SchematicInfo(customName, position, schematicName, fullFileName, id, type));
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

  public static void deleteSchematic(UUID id) {
    schematicInfos.remove(id);
    try {
      File inputFile = new File(idFile);
      File tempFile = new File("schematicIdsTemp.txt");

      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

      String lineToRemove = id.toString();
      String currentLine;

      while ((currentLine = reader.readLine()) != null) {
        String trimmedLine = currentLine.trim();
        if (trimmedLine.contains(lineToRemove)) continue;
        writer.write(currentLine + System.getProperty("line.separator"));
      }
      writer.close();
      reader.close();
      inputFile.delete();
      tempFile.renameTo(inputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
    }