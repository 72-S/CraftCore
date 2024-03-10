package org.craftcore.craftcore.core.block;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import org.craftcore.craftcore.CraftCore;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.Map;

public class BlockStateProperties {
    public static String blockStateToProperties(BlockState blockState) {
        StringBuilder properties = new StringBuilder();

        for (Map.Entry<Property<?>, Comparable<?>> entry : blockState.getEntries().entrySet()) {
            Property<?> property = entry.getKey();
            Comparable<?> value = entry.getValue();

            if (!properties.isEmpty()) {
                properties.append(",");
            }
            properties.append(property.getName());
            properties.append("=");
            properties.append(getPropertyValueAsString(property, value));
        }
        CraftCore.LOGGER.info("Block Properties: " + properties.toString());
        return properties.toString();
    }

    private static <T extends Comparable<T>> String getPropertyValueAsString(Property<T> property, Comparable<?> value) {
        return property.name((T) value);
    }

}
