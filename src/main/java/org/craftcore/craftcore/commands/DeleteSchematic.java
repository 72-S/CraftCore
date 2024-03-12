package org.craftcore.craftcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
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
import org.craftcore.craftcore.core.block.BlockDeleter;
import org.craftcore.craftcore.core.block.BlockDisplayPlacer;
import org.craftcore.craftcore.core.block.BlockParser;
import org.craftcore.craftcore.core.block.BlockPlacer;
import org.craftcore.craftcore.core.shematic.SchematicManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class DeleteSchematic {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("delete")
                        .then(
                                CommandManager.argument("schematicCustomName", StringArgumentType.string())
                                        .suggests(DeleteSchematic::getCustomNameSuggestions)
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
                                                                        }))));

    }


    private static CompletableFuture<Suggestions> getCustomNameSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        SchematicManager.schematicInfos.values().forEach(schematicInfo -> {
            builder.suggest(schematicInfo.customName);
        });
        return builder.buildFuture();
    }

    @FunctionalInterface
    interface BlockHandler {
        void handle(ServerWorld source, BlockState blockState, BlockPos blockPos, Position playerPosition);
    }


    private static int executeWithHandler(CommandContext<ServerCommandSource> context, LoadSchematic.BlockHandler handler, String schematicCustomName) {
        ServerCommandSource source = context.getSource();
        SchematicManager.SchematicInfo schematicInfo = SchematicManager.schematicInfos.values().stream()
                .filter(info -> info.customName.equals(schematicCustomName))
                .findFirst()
                .orElse(null);

        Path schematicPath = Paths.get(SchematicManager.SCHEMATIC_FOLDER_NAME, schematicInfo.fullFileName);
        Path fullPath = FabricLoader.getInstance().getGameDir().resolve(schematicPath);

        if (Files.exists(fullPath)) {
            try {
                Schematic schematic = SchematicLoader.load(fullPath);
                SchematicManager.deleteSchematic(schematicInfo.id);
                source.sendFeedback(() -> Text.literal("Schematic loaded: " + schematicInfo.customName), false);
                source.sendFeedback(() -> Text.literal("Schematic size: " + schematic.length()), false);
                schematic.blocks().forEach(pair -> {
                    SchematicBlockPos schematicBlockPos = pair.left();
                    BlockPos blockPos = new BlockPos(schematicBlockPos.x(), schematicBlockPos.y(), schematicBlockPos.z());
                    SchematicBlock block = pair.right();
                    Position playerPosition = schematicInfo.position;
                    String blockName = block.name();
                    BlockState blockState = BlockParser.parseBlockState(blockName);
                    handler.handle(source.getWorld(), blockState, blockPos, playerPosition);
                });
            } catch (ParsingException | IOException e) {
                source.sendError(Text.literal("Failed to load schematic: " + e.getMessage()));
                return 0;
            }
        } else {
            source.sendError(Text.literal(schematicInfo.fullFileName + " file not found: " + fullPath));
            return 0;
        }
        return 1;
    }


    private static int executeBlock(CommandContext<ServerCommandSource> context) {
        return executeWithHandler(context, BlockDeleter::handleBlock, StringArgumentType.getString(context, "schematicCustomName"));
    }

    private static int executeBlockDisplay(CommandContext<ServerCommandSource> context) {
        return executeWithHandler(context, BlockDisplayPlacer::handleBlock, StringArgumentType.getString(context, "schematicCustomName"));
    }
}
