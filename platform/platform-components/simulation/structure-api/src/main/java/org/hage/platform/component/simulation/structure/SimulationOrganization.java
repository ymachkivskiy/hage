package org.hage.platform.component.simulation.structure;

import lombok.Data;
import org.hage.platform.component.simulation.structure.definition.PopulationDistributionMap;
import org.hage.platform.component.simulation.structure.definition.StructureDefinition;

import java.io.Serializable;

@Data
public class SimulationOrganization implements Serializable {
    private final StructureDefinition structureDefinition;
    private final PopulationDistributionMap populationDistributionMap;
}
