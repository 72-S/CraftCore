package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import org.craftcore.craftcore.CraftCore;

public class BlockPlacer {
  public static void handleBlock(ServerWorld world, BlockState blockName, BlockPos blockPos, Position position) {
    CraftCore.LOGGER.info(
        "Block: "
            + blockName
            + " Position: "
            + blockPos.getX()
            + ", "
            + blockPos.getY()
            + ", "
            + blockPos.getZ()
            + " Player Position: "
            + position.getX()
            + ", "
            + position.getY()
            + ", "
            + position.getZ());

    int newX = (int) (blockPos.getX() + position.getX());
    int newY = (int) (blockPos.getY() + position.getY());
    int newZ = (int) (blockPos.getZ() + position.getZ());
    world.setBlockState(new BlockPos(newX, newY, newZ), blockName);
  }
}
