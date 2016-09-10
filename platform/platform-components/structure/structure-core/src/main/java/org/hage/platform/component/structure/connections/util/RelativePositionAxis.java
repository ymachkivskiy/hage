package org.hage.platform.component.structure.connections.util;

import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.connections.RelativePosition;

import java.util.EnumMap;

import static org.hage.platform.component.structure.Axis.*;
import static org.hage.platform.component.structure.connections.RelativePosition.*;

public class RelativePositionAxis {
    private static final EnumMap<RelativePosition, Axis> relativePositionAxisMap = new EnumMap<>(RelativePosition.class);

    static {
        relativePositionAxisMap.put(ABOVE, VERTICAL);
        relativePositionAxisMap.put(BELOW, VERTICAL);
        relativePositionAxisMap.put(ON_LEFT, HORIZONTAL);
        relativePositionAxisMap.put(ON_RIGHT, HORIZONTAL);
        relativePositionAxisMap.put(IN_FRONT, DEPTH);
        relativePositionAxisMap.put(AT_BACK, DEPTH);
    }

    public static Axis axisOf(RelativePosition relativePosition) {
        return relativePositionAxisMap.get(relativePosition);
    }

}
