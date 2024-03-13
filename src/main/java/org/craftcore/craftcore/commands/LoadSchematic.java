package org.craftcore.craftcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;
import org.craftcore.craftcore.core.block.BlockDisplayPlacer;
import org.craftcore.craftcore.core.block.BlockParser;
import org.craftcore.craftcore.core.block.BlockPlacer;
import org.craftcore.craftcore.core.shematic.SchematicManager;

public class LoadSchematic {
    private static Boolean Type = false;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("load")
                        .then(
                                CommandManager.argument("schematicName", StringArgumentType.string())
                                        .then(
                                                CommandManager.argument("exampleChoice", StringArgumentType.string())
                                                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                                                new String[] {"schem", "schematic", "litematic"}, builder))
                                                        .then(
                                                                CommandManager.argument("blockType", StringArgumentType.string())
                                                                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                                                                new String[] {"block", "blockdisplay"}, builder))
                                                                        .executes(context -> {
                                                                            String blockType = StringArgumentType.getString(context, "blockType");
                                                                            if ("block".equals(blockType)) {
                                                                                return executeBlock(context);
                                                                            } else if ("blockdisplay".equals(blockType)) {
                                                                                return executeBlockDisplay(context);
                                                                            } else {
                                                                                context.getSource().sendError(Text.literal("Invalid block type!"));
                                                                                return 0;
                                                                            }
                                                                        })))));
    }

    @FunctionalInterface
    interface BlockHandler {
        void handle(ServerWorld source, BlockState blockState, BlockPos blockPos, Position playerPosition, UUID id);
    }


    private static int executeWithHandler(CommandContext<ServerCommandSource> context, BlockHandler handler) {
        ServerCommandSource source = context.getSource();
        String schematicName = StringArgumentType.getString(context, "schematicName");
        String fileType = StringArgumentType.getString(context, "exampleChoice");

        String fullFileName = schematicName + "." + fileType;
        Path schematicPath = Paths.get(SchematicManager.SCHEMATIC_FOLDER_NAME, fullFileName);
        Path fullPath = FabricLoader.getInstance().getGameDir().resolve(schematicPath);

        if (Files.exists(fullPath)) {
            try {
                Schematic schematic = SchematicLoader.load(fullPath);
                SchematicManager.saveSchematicId(source.getPosition(), schematicName, fullFileName, schematicName+".test", Type);
                source.sendFeedback(() -> Text.literal("Schematic loaded: " + schematicName), false);
                source.sendFeedback(() -> Text.literal("Schematic size: " + schematic.length()), false);
                schematic.blocks().forEach(pair -> {
                    SchematicBlockPos schematicBlockPos = pair.left();
                    BlockPos blockPos = new BlockPos(schematicBlockPos.x(), schematicBlockPos.y(), schematicBlockPos.z());
                    SchematicBlock block = pair.right();
                    Position playerPosition = source.getPosition();
                    String blockName = block.name();
                    BlockState blockState = BlockParser.parseBlockState(blockName);
                    UUID id = Objects.requireNonNull(SchematicManager.schematicInfos.values().stream()
                            .filter(info -> info.fullFileName.equals(fullFileName))
                            .findFirst()
                            .orElse(null)).id;

                    handler.handle(source.getWorld(), blockState, blockPos, playerPosition, id);
                });
            } catch (ParsingException | IOException e) {
                source.sendError(Text.literal("Failed to load schematic: " + e.getMessage()));
                return 0;
            }
        } else {
            source.sendError(Text.literal(fileType + " file not found: " + fullPath));
            return 0;
        }
        return 1;
    }


    private static int executeBlock(CommandContext<ServerCommandSource> context) {
        Type = true;
        return executeWithHandler(context, BlockPlacer::handleBlock);
    }

    private static int executeBlockDisplay(CommandContext<ServerCommandSource> context) {
        Type = false;
        return executeWithHandler(context, BlockDisplayPlacer::handleBlock);
    }

}
