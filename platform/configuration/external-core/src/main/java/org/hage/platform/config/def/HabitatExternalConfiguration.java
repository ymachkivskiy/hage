package org.hage.platform.config.def;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.habitat.structure.StructureDefinition;

import java.util.List;

@Builder
public final class HabitatExternalConfiguration {
    @Getter
    private final StructureDefinition structureDefinition;
    @Getter
    private final List<PopulationQualifier> populationDistribution;
}
