package org.hage.platform.habitat.structure;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StructureDefinition implements Serializable {
    private final Dimensions habitatDimensions;
    private final BoundaryConditions boundaryConditions;
    private final BigDecimal fragmentSideSize;
}
