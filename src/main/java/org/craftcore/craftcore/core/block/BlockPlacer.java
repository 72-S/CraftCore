package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

import java.util.UUID;

public class BlockPlacer {
  public static void handleBlock(ServerWorld world, BlockState blockName, BlockPos blockPos, Position position, UUID uuid) {
    world.setBlockState(BlockCoordinateHandler.getNewCoordinates(blockPos, position, blockName), blockName);
  }
}
