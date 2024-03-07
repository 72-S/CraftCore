package org.craftcore.craftcore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.nbt.NbtCompound;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.craftcore.craftcore.CraftCore;
import org.craftcore.craftcore.core.ShematicManager;
import net.minecraft.nbt.NbtSizeTracker;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LoadShem {

    public static final Map<String, NbtCompound> loadedSchematics = new HashMap<>();

        public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            dispatcher.register(
                    CommandManager.literal("loadschematic")
                            .then(CommandManager.argument("schematicName", StringArgumentType.string())
                                    .executes(context -> execute(context))
                            )
            );
        }



        private static int execute(CommandContext<ServerCommandSource> context) {
            ServerCommandSource source = context.getSource();
            // Nutzen Sie `StringArgumentType.getString` um den Wert des Arguments zu erhalten
            String schematicName = StringArgumentType.getString(context, "schematicName");
            Path shematicPath = Paths.get(ShematicManager.SCHEMATIC_FOLDER_NAME, schematicName);
            Path fullPath = FabricLoader.getInstance().getGameDir().resolve(shematicPath);

            if (Files.exists(fullPath)) {
                File schematicFile = fullPath.toFile();
                try (FileInputStream fis = new FileInputStream(schematicFile)) {
                    NbtCompound schematicTag = NbtIo.readCompressed(fis, NbtSizeTracker.ofUnlimitedBytes());
                    // Speichern Sie das NbtCompound-Objekt in der Map
                    loadedSchematics.put(schematicName, schematicTag);
                    source.sendFeedback(() -> Text.literal("Schematic loaded: " + schematicName), false);
                    CraftCore.LOGGER.info("Loaded schematic: " + schematicName + ", size: " + loadedSchematics.size() + loadedSchematics);

                } catch (Exception e) {
                    source.sendError(Text.literal("Failed to load schematic: " + e.getMessage()));
                    e.printStackTrace();
                }
            } else {
                source.sendError(Text.literal("Schematic file not found." + fullPath.toString()));
            }

            return Command.SINGLE_SUCCESS;
        }

        public static NbtCompound getSchematic(String name) {
                return loadedSchematics.get(name);
        }
    }



