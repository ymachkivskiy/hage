package org.hage.platform.habitat.structure;

import lombok.Builder;
import lombok.Data;
import org.hage.platform.habitat.structure.BoundaryConditions;
import org.hage.platform.habitat.structure.Dimensions;

import java.math.BigDecimal;

@Data
@Builder
public final class StructureDefinition {
    private final Dimensions habitatDimensions;
    private final BoundaryConditions boundaryConditions;
    private final BigDecimal fragmentSideSize;
}
