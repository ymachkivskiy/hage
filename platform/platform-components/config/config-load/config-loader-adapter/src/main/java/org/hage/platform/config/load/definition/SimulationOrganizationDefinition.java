package org.hage.platform.config.load.definition;

import lombok.Data;
import org.hage.platform.component.structure.connections.StructureDefinition;

import java.util.List;

@Data
public final class SimulationOrganizationDefinition {
    private final StructureDefinition structureDefinition;
    private final List<ChunkPopulationQualifier> chunkPopulationQualifiers;
}
