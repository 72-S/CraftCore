package org.craftcore.craftcore.core;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicNamed;
import org.craftcore.craftcore.CraftCore;

import java.util.List;

public class BlockPlacer {
    public void handleBlock(ServerWorld world, BlockState blockName, BlockPos blockPos, Position position) {
                CraftCore.LOGGER.info("Block: " + blockName + " Position: " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + " Player Position: " + position.getX() + ", " + position.getY() + ", " + position.getZ());

        int newx = (int) (blockPos.getX() + position.getX());
        int newy = (int) (blockPos.getY() + position.getY());
        int newz = (int) (blockPos.getZ() + position.getZ());
                if (blockName.equals("minecraft:air")) {
                    return;
                }else {
                    world.setBlockState(new BlockPos(newx, newy, newz), blockName);

                }

    }
}
