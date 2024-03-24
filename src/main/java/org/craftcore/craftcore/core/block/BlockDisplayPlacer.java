package org.craftcore.craftcore.core.block;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import org.craftcore.craftcore.CraftCore;

public class BlockDisplayPlacer {
  public static void handleBlock(ServerWorld world, BlockState blockState, BlockPos blockPos, Position position, UUID uuid) {
    int newX = (int) (blockPos.getX() + position.getX());
    int newY = (int) (blockPos.getY() + position.getY());
    int newZ = (int) (blockPos.getZ() + position.getZ());
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    NbtCompound blockStateTag = new NbtCompound();
    blockStateTag.putString("Name", BlockStateString.blockStateToString(blockState));

    NbtCompound blockProperties = new NbtCompound();
    for (Map.Entry<Property<?>, Comparable<?>> entry : blockState.getEntries().entrySet()) {
        Property<?> property = entry.getKey();
        Comparable<?> value = entry.getValue();
        blockProperties.putString(property.getName(), propertyAsString(property, value));
    }
    blockStateTag.put("Properties", blockProperties);
    NbtCompound entityData = new NbtCompound();
    entityData.put("block_state", blockStateTag);
      Entity entity = EntityType.BLOCK_DISPLAY.create(world);
      if (entity != null) {
          entity.readNbt(entityData);
          entity.setPos(newX, newY, newZ);
          entity.addCommandTag(uuid.toString());
          entity.setInvulnerable(true);
          // In der BlockDisplayPlacer-Klasse
          if (!world.isClient) {
              world.spawnEntity(entity);



          } else {
              CraftCore.LOGGER.info("BlockDisplayPlacer: Client side");
          }

        executorService.shutdown();
      }

  }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String propertyAsString(Property<T> property, Comparable<?> value) {
        return property.name((T) value);
    }
}
