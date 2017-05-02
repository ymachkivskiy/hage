package org.hage.platform.component.structure.connections.grid.config;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.StructureException;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static java.util.EnumSet.*;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.component.structure.Axis.*;
import static org.hage.platform.component.structure.grid.GridBoundaryConditions.*;

@RequiredArgsConstructor(access = PRIVATE)
public class BoundaryAxisMirroringConfig {

    private static final Map<GridBoundaryConditions, EnumSet<Axis>> allowedShiftAxisesMap;

    static {
        allowedShiftAxisesMap = new EnumMap<>(ImmutableMap.<GridBoundaryConditions, EnumSet<Axis>>builder()
            .put(CLOSED, noneOf(Axis.class))
            .put(DEPTH__TORUS, of(DEPTH))
            .put(VERTICAL__TORUS, of(VERTICAL))
            .put(HORIZONTAL__TORUS, of(HORIZONTAL))
            .put(DEPTH_AND_HORIZONTAL__TORUS, of(DEPTH, HORIZONTAL))
            .put(DEPTH_AND_VERTICAL__TORUS, of(DEPTH, VERTICAL))
            .put(HORIZONTAL_AND_VERTICAL__TORUS, of(HORIZONTAL, VERTICAL))
            .put(FULL__TORUS, allOf(Axis.class))
            .build()
        );
    }

    private static EnumSet<Axis> allowedAxisesForMirroring(GridBoundaryConditions gridBoundaryConditions) {
        return ofNullable(allowedShiftAxisesMap.get(gridBoundaryConditions))
            .orElseThrow(() -> new StructureException("Allowed wrapped axises for boundary conditions " + gridBoundaryConditions + " are not specified"));
    }

    public static EnumSet<Axis> axisesForbiddenForMirroring(GridBoundaryConditions gridBoundaryConditions) {
        return complementOf(allowedAxisesForMirroring(gridBoundaryConditions));
    }

}
