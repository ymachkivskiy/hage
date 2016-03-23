package org.hage.platform.component.structure.connections.grid;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Axis;
import org.hage.platform.component.structure.StructureException;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.*;
import static java.util.EnumSet.allOf;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.component.structure.connections.grid.BoundaryConditions.CLOSED;
import static org.hage.platform.component.structure.connections.grid.BoundaryConditions.FULL_TORUS;

@RequiredArgsConstructor(access = PRIVATE)
public class BoundaryConditionsWrappingConfig {

    private static final Map<BoundaryConditions, Set<Axis>> allowedShiftAxisesMap;

    static {
        allowedShiftAxisesMap = unmodifiableMap(of(
            CLOSED, emptySet(),
            FULL_TORUS, unmodifiableSet(allOf(Axis.class))
        ));
    }

    public static Set<Axis> allowedWrappedShiftsAxises(BoundaryConditions boundaryConditions) {
        return ofNullable(allowedShiftAxisesMap.get(boundaryConditions))
            .orElseThrow(() -> new StructureException("Allowed wrapped axises for boundary conditions " + boundaryConditions + " are not specified"));
    }

}
