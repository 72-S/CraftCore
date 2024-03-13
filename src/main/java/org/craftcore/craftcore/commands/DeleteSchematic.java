package org.craftcore.craftcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;
import org.craftcore.craftcore.CraftCore;
import org.craftcore.craftcore.core.block.*;
import org.craftcore.craftcore.core.shematic.SchematicManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class DeleteSchematic {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("delete")
                        .then(
                                CommandManager.argument("schematicCustomName", StringArgumentType.string())
                                        .suggests(DeleteSchematic::getCustomNameSuggestions)
                                        .executes(context -> execute(context, StringArgumentType.getString(context, "schematicCustomName")))));

    }

    private static CompletableFuture<Suggestions> getCustomNameSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        SchematicManager.schematicInfos.values().forEach(schematicInfo -> builder.suggest(schematicInfo.customName));
        return builder.buildFuture();
    }

    private static int execute(CommandContext<ServerCommandSource> context, String schematicCustomName) {
        ServerCommandSource source = context.getSource();
        SchematicManager.SchematicInfo schematicInfo = SchematicManager.schematicInfos.values().stream()
                .filter(info -> info.customName.equals(schematicCustomName))
                .findFirst()
                .orElse(null);
        assert schematicInfo != null;
        String fullFileName = schematicInfo.fullFileName;
        Path path = FabricLoader.getInstance().getGameDir().resolve(SchematicManager.SCHEMATIC_FOLDER_NAME).resolve(fullFileName);

        if (Files.exists(path)) {
            try{
                Schematic schematic = SchematicLoader.load(path);
                if (schematicInfo.type == true) {
                    schematic.blocks().forEach(pair -> {
                        SchematicBlock schematicBlock = pair.right();
                        SchematicBlockPos pos = pair.left();
                        BlockState block = BlockParser.parseBlockState(schematicBlock.name());
                        BlockDeleter.handleBlock(source.getWorld(), block, new BlockPos(pos.x(), pos.y(), pos.z()), schematicInfo.position);
                    });

                } else {
                    schematic.blocks().forEach(pair -> {
                        SchematicBlock schematicBlock = pair.right();
                        SchematicBlockPos pos = pair.left();
                        BlockState block = BlockParser.parseBlockState(schematicBlock.name());
                        BlockDisplayDeleter.handleBlock(source.getWorld(), block, new BlockPos(pos.x(), pos.y(), pos.z()), schematicInfo.position, schematicInfo.id);
                    });

                }


            } catch (IOException e) {
                source.sendError(Text.literal("Error deleting schematic " + schematicCustomName));
            } catch (ParsingException e) {
                throw new RuntimeException(e);
            }
        } else {
            source.sendError(Text.literal("Schematic " + schematicCustomName + " does not exist"));
        }

        return 1;
    }


}
