package org.hage.platform.config.habitat;

import lombok.Builder;
import lombok.Getter;

@Builder
public final class HabitatConfiguration {
    @Getter
    private final StructureDefinition structureDefinition;
    @Getter
    private final PopulationDistribution populationDistribution;
}
