package org.hage.platform.component.structure.definition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.component.structure.definition.StructureType.BOX;
import static org.hage.platform.component.structure.definition.StructureType.GRAPH;

@RequiredArgsConstructor(access = PRIVATE)
@ToString
@Getter
public class StructureDefinition implements Serializable {
    private final StructureType structureType;
    private final BoundaryConditions boundaryConditions;
    private final Dimensions boxDimensions;
    private final List<Pair<Position, Position>> graphDefinition;

    public StructureDefinition(BoundaryConditions boundaryConditions, Dimensions dimensions) {
        this(BOX, boundaryConditions, dimensions, emptyList());
    }

    public StructureDefinition(BoundaryConditions boundaryConditions, List<Pair<Position, Position>> graphDefinition) {
        this(GRAPH, boundaryConditions, null, graphDefinition);
    }

}
