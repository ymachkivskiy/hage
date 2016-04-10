package org.hage.platform.component.simulationconfig.load.definition;

import lombok.Data;
import org.hage.platform.component.structure.StructureDefinition;

import java.util.List;

@Data
public final class SimulationOrganizationDefinition {
    private final StructureDefinition structureDefinition;
    private final List<ChunkPopulationQualifier> chunkPopulationQualifiers;
}
