package org.hage.platform.component.structure.connections.grid.config;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.StructureException;
import org.hage.platform.component.structure.connections.grid.BoundaryConditions;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.EnumSet.*;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.component.structure.connections.grid.BoundaryConditions.CLOSED;
import static org.hage.platform.component.structure.connections.grid.BoundaryConditions.FULL_TORUS;

@RequiredArgsConstructor(access = PRIVATE)
public class BoundaryAxisMirroringConfig {

    private static final Map<BoundaryConditions, EnumSet<Axis>> allowedShiftAxisesMap;

    static {
        allowedShiftAxisesMap = new EnumMap<>(of(
            CLOSED, noneOf(Axis.class),
            FULL_TORUS, allOf(Axis.class)
        ));
    }

    private static EnumSet<Axis> allowedAxisesForMirroring(BoundaryConditions boundaryConditions) {
        return ofNullable(allowedShiftAxisesMap.get(boundaryConditions))
            .orElseThrow(() -> new StructureException("Allowed wrapped axises for boundary conditions " + boundaryConditions + " are not specified"));
    }

    public static EnumSet<Axis> axisesForbiddenForMirroring(BoundaryConditions boundaryConditions) {
        return complementOf(allowedAxisesForMirroring(boundaryConditions));
    }

}
