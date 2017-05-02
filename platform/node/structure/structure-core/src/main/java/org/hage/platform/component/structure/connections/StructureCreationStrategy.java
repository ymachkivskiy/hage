package org.hage.platform.component.structure.connections;

import org.hage.platform.component.structure.StructureDefinition;

public interface StructureCreationStrategy {
    boolean isApplicableFor(StructureDefinition structure);

    Structure createUsingDefinition(StructureDefinition structure);
}
