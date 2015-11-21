package org.hage.platform.habitat.structure;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StructureDefinition {
    private final Dimensions habitatDimensions;
    private final BoundaryConditions boundaryConditions;
    private final BigDecimal fragmentSideSize;
}
