package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import org.craftcore.craftcore.CraftCore;


public class BlockDeleter {
    public static void handleBlock(ServerWorld world, BlockState blockName, BlockPos blockPos, Position position) {
        CraftCore.LOGGER.info("BlockDeleter Called");
        BlockState oldBlock = world.getBlockState(BlockCoordinateHandler.getNewCoordinates(blockPos, position, blockName));

            world.removeBlock(BlockCoordinateHandler.getNewCoordinates(blockPos, position, blockName), false);

    }
}
