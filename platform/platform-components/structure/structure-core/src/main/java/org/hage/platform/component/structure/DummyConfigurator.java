package org.hage.platform.component.structure;

import org.hage.platform.component.structure.definition.StructureDefinition;
import org.springframework.stereotype.Component;

@Component
public class DummyConfigurator implements StructureRepositoryConfigurator {

    @Override
    public void configure(StructureDefinition structureDefinition) {

        boolean f = false;

    }
}
