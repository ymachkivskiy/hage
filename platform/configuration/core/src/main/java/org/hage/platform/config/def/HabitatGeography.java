package org.hage.platform.config.def;

import lombok.Data;
import org.hage.platform.habitat.structure.StructureDefinition;

import java.io.Serializable;

@Data
public class HabitatGeography implements Serializable {
    private final StructureDefinition structureDefinition;
    private final PopulationDistributionMap populationDistributionMap;
}
