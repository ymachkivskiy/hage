package org.hage.platform.component.simulation.structure.definition;

import lombok.Data;

import java.io.Serializable;

@Data
public class StructureDefinition implements Serializable {
    private final Dimensions habitatDimensions;
    private final BoundaryConditions boundaryConditions;
}
