package org.hage.platform.component.structure.grid;

import org.hage.platform.component.structure.definition.StructureDefinition;

public interface RepositoryCreationStrategy {
    boolean isApplicableFor(StructureDefinition structureDefinition);

    GridConnectionsRepository create(StructureDefinition structureDefinition);
}
