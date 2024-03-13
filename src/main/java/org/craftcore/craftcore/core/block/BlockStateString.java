package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class BlockStateString {
    public static String blockStateToString(BlockState blockState) {
        Identifier blockIdentifier = Registries.BLOCK.getId(blockState.getBlock());
        return blockIdentifier.toString();
  }
}
