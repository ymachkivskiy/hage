package org.hage.platform.config.def;

import lombok.Data;
import org.hage.platform.habitat.structure.StructureDefinition;

@Data
public class HabitatInternalConfiguration {
    private final StructureDefinition structureDefinition;
    private final PopulationDistribution populationDistribution;
}
