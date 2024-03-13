package org.craftcore.craftcore.core.Position;

import net.minecraft.util.math.Position;

public class PositionToStringParser {
    public static String parsePosition(Position position) {
        return "(" + position.getX() + ", " + position.getY() + ", " + position.getZ() + ")";
    }
}
