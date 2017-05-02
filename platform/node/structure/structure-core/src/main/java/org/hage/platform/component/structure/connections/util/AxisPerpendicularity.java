package org.hage.platform.component.structure.connections.util;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Axis;

import java.util.EnumMap;

import static org.hage.platform.component.structure.Axis.*;

public class AxisPerpendicularity {
    private final static EnumMap<Axis, Plane> axisPerpendicularityMap = new EnumMap<>(Axis.class);

    static {
        axisPerpendicularityMap.put(VERTICAL, new Plane(HORIZONTAL, DEPTH));
        axisPerpendicularityMap.put(DEPTH, new Plane(HORIZONTAL, VERTICAL));
        axisPerpendicularityMap.put(HORIZONTAL, new Plane(VERTICAL, DEPTH));
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
