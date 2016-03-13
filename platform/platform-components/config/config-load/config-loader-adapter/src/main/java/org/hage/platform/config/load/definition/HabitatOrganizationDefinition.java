package org.hage.platform.config.load.definition;

import lombok.Data;
import org.hage.platform.component.simulation.structure.definition.StructureDefinition;

import java.util.List;

@Data
public final class HabitatOrganizationDefinition {
    private final StructureDefinition structureDefinition;
    private final List<ChunkPopulationQualifier> chunkPopulationQualifiers;
}
