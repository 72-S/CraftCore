package org.craftcore.craftcore.core.Position;

import net.minecraft.util.math.Position;

public class StringToPositionParser {
    public static Position parsePosition(String input) {
        String[] split = input.replaceAll("[()]", "").split(",\\s*");

        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);

        return new Position() {
            @Override
            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }

            @Override
            public double getZ() {
                return z;
            }
        };
    }
}
