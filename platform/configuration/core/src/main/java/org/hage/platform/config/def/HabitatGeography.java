package org.hage.platform.config.def;

import lombok.Data;
import org.hage.platform.habitat.structure.StructureDefinition;

@Data
public class HabitatGeography {
    private final StructureDefinition structureDefinition;
    private final PopulationDistributionMap populationDistributionMap;
}
