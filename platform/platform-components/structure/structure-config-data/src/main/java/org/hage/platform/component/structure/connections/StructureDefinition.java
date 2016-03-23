package org.hage.platform.component.structure.connections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.component.structure.connections.graph.Edge;
import org.hage.platform.component.structure.connections.graph.GraphDefinition;
import org.hage.platform.component.structure.connections.graph.GraphType;
import org.hage.platform.component.structure.connections.grid.BoundaryConditions;
import org.hage.platform.component.structure.connections.grid.Dimensions;
import org.hage.platform.component.structure.connections.grid.GridDefinition;
import org.hage.platform.component.structure.connections.grid.GridNeighborhoodType;

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

    public StructureDefinition(BoundaryConditions boundaryConditions, Dimensions dimensions, GridNeighborhoodType gridNeighborhoodType) {
        this(StructureType.FULL_GRID, new GridDefinition(boundaryConditions, dimensions, gridNeighborhoodType), null);
    }

    public StructureDefinition(List<Position> islands) {
        this(StructureType.GRAPH, null, new GraphDefinition(islands));
    }

    public StructureDefinition(GraphType graphType, List<Position> vertexes, List<Edge> edges) {
        this(StructureType.GRAPH, null, new GraphDefinition(graphType, vertexes, edges));
    }

}
