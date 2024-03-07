package org.craftcore.craftcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.craftcore.craftcore.CraftCore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PasteShem {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("pasteschematic")
                        .then(CommandManager.argument("schematicName", StringArgumentType.string())
                                .executes(context -> execute(context))
                        )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.of("Player required")).create();
        }

        String schematicName = StringArgumentType.getString(context, "schematicName");
        Path schematicPath = Paths.get("schematics", schematicName + ".schematic");

        if (!Files.exists(schematicPath)) {
            context.getSource().sendError(Text.literal("Schematic file not found: " + schematicPath));
            return 0;
        }

        try {
            NbtCompound schematicTag = readSchematic(schematicPath);
            placeSchematic((ServerWorld) ((ServerPlayerEntity) player).getWorld(), player.getPos(), schematicTag);
            context.getSource().sendFeedback(() -> Text.literal("Schematic pasted: " + schematicName), false);
            return 1;
        } catch (IOException e) {
            CraftCore.LOGGER.error("Failed to paste schematic: " + e.getMessage());
            context.getSource().sendError(Text.literal("Failed to paste schematic"));
            return 0;
        }
    }

    private static NbtCompound readSchematic(Path schematicPath) throws IOException {
        File schematicFile = schematicPath.toFile();
        try (FileInputStream fis = new FileInputStream(schematicFile)) {
            return NbtIo.readCompressed(fis, NbtSizeTracker.ofUnlimitedBytes());
        }
    }

    private static void placeSchematic(ServerWorld world, Vec3d playerPos, NbtCompound schematic) {
        NbtCompound blocks = schematic.getCompound("Blocks");
        BlockPos origin = new BlockPos((int) playerPos.x, (int)playerPos.y, (int)playerPos.z);

        for (String key : blocks.getKeys()) {
            BlockPos blockPos = BlockPos.fromLong(Long.parseLong(key));
            BlockState blockState = decodeBlockState(blocks.getCompound(key));
            if (blockState != null) {
                world.setBlockState(origin.add(blockPos), blockState, 3); // Using 3 for update flags
            } else {
                CraftCore.LOGGER.warn("Failed to place block at " + blockPos + ", invalid block state");
            }
        }
    }

    private static BlockState decodeBlockState(NbtCompound blockTag) {
        DataResult<BlockState> result = BlockState.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, blockTag));
        return result.result().orElse(null); // Ersetzen von `null` mit einem Standard-BlockState

    }
}