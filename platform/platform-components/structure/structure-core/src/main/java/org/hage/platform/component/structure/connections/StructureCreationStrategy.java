package org.hage.platform.component.structure.connections;

public interface StructureCreationStrategy {
    boolean isApplicableFor(StructureDefinition structure);

    Structure createUsingDefinition(StructureDefinition structure);
}
