package org.hage.platform.node.structure.connections;

import org.hage.platform.node.structure.StructureDefinition;

public interface StructureCreationStrategy {
    boolean isApplicableFor(StructureDefinition structure);

    Structure createUsingDefinition(StructureDefinition structure);
}
