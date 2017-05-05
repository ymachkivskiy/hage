package org.hage.platform.node.structure.connections.util;

import org.hage.platform.node.structure.Axis;
import org.hage.platform.node.structure.connections.RelativePosition;

import java.util.EnumMap;

public class RelativePositionAxis {
    private static final EnumMap<RelativePosition, Axis> relativePositionAxisMap = new EnumMap<>(RelativePosition.class);

    static {
        relativePositionAxisMap.put(RelativePosition.ABOVE, Axis.VERTICAL);
        relativePositionAxisMap.put(RelativePosition.BELOW, Axis.VERTICAL);
        relativePositionAxisMap.put(RelativePosition.ON_LEFT, Axis.HORIZONTAL);
        relativePositionAxisMap.put(RelativePosition.ON_RIGHT, Axis.HORIZONTAL);
        relativePositionAxisMap.put(RelativePosition.IN_FRONT, Axis.DEPTH);
        relativePositionAxisMap.put(RelativePosition.AT_BACK, Axis.DEPTH);
    }

    public static Axis axisOf(RelativePosition relativePosition) {
        return relativePositionAxisMap.get(relativePosition);
    }

}
