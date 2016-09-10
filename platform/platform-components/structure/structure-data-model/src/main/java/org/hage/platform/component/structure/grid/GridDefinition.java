package org.hage.platform.component.structure.grid;

import lombok.Data;

import java.io.Serializable;

@Data
public class GridDefinition implements Serializable {
    private final GridBoundaryConditions gridBoundaryConditions;
    private final Dimensions dimensions;
    private final GridNeighborhoodType neighborhoodType;
}
