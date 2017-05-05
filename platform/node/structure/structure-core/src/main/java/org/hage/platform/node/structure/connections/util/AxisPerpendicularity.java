package org.hage.platform.node.structure.connections.util;

import lombok.RequiredArgsConstructor;
import org.hage.platform.node.structure.Axis;

import java.util.EnumMap;

public class AxisPerpendicularity {
    private final static EnumMap<Axis, Plane> axisPerpendicularityMap = new EnumMap<>(Axis.class);

    static {
        axisPerpendicularityMap.put(Axis.VERTICAL, new Plane(Axis.HORIZONTAL, Axis.DEPTH));
        axisPerpendicularityMap.put(Axis.DEPTH, new Plane(Axis.HORIZONTAL, Axis.VERTICAL));
        axisPerpendicularityMap.put(Axis.HORIZONTAL, new Plane(Axis.VERTICAL, Axis.DEPTH));
    }

    public static Plane planePerpendicularTo(Axis axis) {
        return axisPerpendicularityMap.get(axis);
    }

    @RequiredArgsConstructor
    public static class Plane {
        public final Axis firstAxis;
        public final Axis secondAxis;
    }
}
