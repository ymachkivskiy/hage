package org.hage.platform.config.def;

import lombok.Data;
import org.hage.platform.habitat.structure.StructureDefinition;

import java.util.List;

@Data
public final class HabitatOrganizationDefinition {
    private final StructureDefinition structureDefinition;
    private final List<ChunkPopulationQualifier> chunkPopulationQualifiers;
}
