package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.querz.nbt.tag.CompoundTag;
import org.craftcore.craftcore.CraftCore;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class BlockDisplayDeleter {
  public static void handleBlock(ServerWorld world, BlockState blockName, BlockPos blockPos, Position position, UUID uuid) {
    CraftCore.LOGGER.info("BlockDisplayDeleter Called");
    BlockState oldBlock = world.getBlockState(BlockCoordinateHandler.getNewCoordinates(blockPos, position, blockName));

     BlockPos newBlockPos = BlockCoordinateHandler.getNewCoordinates(blockPos, position, blockName);
        int newX = newBlockPos.getX();
        int newY = newBlockPos.getY();
        int newZ = newBlockPos.getZ();
        CraftCore.LOGGER.info("Position: " + newX + ", " + newY + ", " + newZ);

        Box searchBox = new Box(newX - 2, newY - 2, newZ - 2, newX + 2, newY + 2, newZ + 2);
        CraftCore.LOGGER.info("Box: " + searchBox);
        for (Entity entity : world.getEntitiesByClass(Entity.class, searchBox, (entity) -> true)) {
            CraftCore.LOGGER.info("Entity Box Found");
            Set<String> tags = entity.getCommandTags();
            Iterator<String> iterator = tags.iterator();
            String firstTag = "";
            if (iterator.hasNext()) {
                firstTag = iterator.next();
            }

            CraftCore.LOGGER.info("Entity UUID: " + firstTag);
          if (Objects.equals(firstTag, uuid.toString())) {
              CraftCore.LOGGER.info("Entity Found");
              entity.remove(Entity.RemovalReason.DISCARDED);
          }
      }

    }
  }

