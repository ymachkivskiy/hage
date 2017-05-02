package org.hage.platform.component.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.component.structure.graph.Edge;
import org.hage.platform.component.structure.graph.GraphDefinition;
import org.hage.platform.component.structure.graph.GraphType;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;
import org.hage.platform.component.structure.grid.Dimensions;
import org.hage.platform.component.structure.grid.GridDefinition;
import org.hage.platform.component.structure.grid.GridNeighborhoodType;

import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
@ToString
@Getter
public class StructureDefinition implements Serializable {
    private final StructureType structureType;
    private final GridDefinition gridDefinition;
    private final GraphDefinition graphDefinition;

    public StructureDefinition(GridBoundaryConditions gridBoundaryConditions, Dimensions dimensions, GridNeighborhoodType gridNeighborhoodType) {
        this(StructureType.FULL_GRID, new GridDefinition(gridBoundaryConditions, dimensions, gridNeighborhoodType), null);
    }

    public StructureDefinition(List<Position> islands) {
        this(StructureType.GRAPH, null, new GraphDefinition(islands));
    }

    public StructureDefinition(GraphType graphType, List<Position> vertexes, List<Edge> edges) {
        this(StructureType.GRAPH, null, new GraphDefinition(graphType, vertexes, edges));
    }

}
