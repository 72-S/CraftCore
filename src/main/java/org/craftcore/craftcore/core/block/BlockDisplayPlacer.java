package org.craftcore.craftcore.core.block;

import java.util.Map;
import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import org.craftcore.craftcore.CraftCore;

public class BlockDisplayPlacer {
  public static void handleBlock(ServerWorld world, BlockState blockState, BlockPos blockPos, Position position, UUID uuid) {
    int newX = (int) (blockPos.getX() + position.getX());
    int newY = (int) (blockPos.getY() + position.getY());
    int newZ = (int) (blockPos.getZ() + position.getZ());
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
      Entity entity = Registries.ENTITY_TYPE.get(new Identifier("minecraft", "block_display")).create(world);
      if (entity != null) {

          entity.readNbt(entityData);
          entity.setPos(newX, newY, newZ);
          entity.addCommandTag(uuid.toString());
          world.spawnEntity(entity);
      }

  }

    private static <T extends Comparable<T>> String propertyAsString(Property<T> property, Comparable<?> value) {
        return property.name((T) value);
    }
}
