package org.hage.platform.config.habitat;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class StructureDefinition {
    private final int width;
    private final int height;
    private final int depth;

    private final BigDecimal fragmentSideSize;
}
