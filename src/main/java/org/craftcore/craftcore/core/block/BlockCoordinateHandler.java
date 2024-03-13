package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import org.craftcore.craftcore.CraftCore;

public class BlockCoordinateHandler {
    public static BlockPos getNewCoordinates(BlockPos blockPos, Position position, BlockState blockName) {

//        CraftCore.LOGGER.info(
//                "Block: "
//                        + blockName.toString()
//                        + " Position: "
//                        + blockPos.getX()
//                        + ", "
//                        + blockPos.getY()
//                        + ", "
//                        + blockPos.getZ()
//                        + " Player Position: "
//                        + position.getX()
//                        + ", "
//                        + position.getY()
//                        + ", "
//                        + position.getZ());



        int newX = (int) (blockPos.getX() + position.getX());
        int newY = (int) (blockPos.getY() + position.getY());
        int newZ = (int) (blockPos.getZ() + position.getZ());
        return new BlockPos(newX, newY, newZ);
    }
}
