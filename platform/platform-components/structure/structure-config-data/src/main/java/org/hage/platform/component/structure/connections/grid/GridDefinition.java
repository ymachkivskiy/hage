package org.hage.platform.component.structure.connections.grid;

import lombok.Data;

import java.io.Serializable;

@Data
public class GridDefinition implements Serializable {
    private final BoundaryConditions boundaryConditions;
    private final Dimensions dimensions;
    private final GridNeighborhoodType neighborhoodType;
}
