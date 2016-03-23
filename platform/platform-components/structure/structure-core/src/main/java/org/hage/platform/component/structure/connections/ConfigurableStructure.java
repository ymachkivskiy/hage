package org.hage.platform.component.structure.connections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class ConfigurableStructure implements Structure, StructureConfigurator {

    @Autowired
    private StructureCreator structureCreator;

    private Structure structure;

    @Override
    public boolean belongsToStructure(Position position) {
        checkState(structure != null, "Structure not initialized");

        return structure.belongsToStructure(position);
    }

    @Override
    public boolean areNeighbors(Position first, Position second) {
        checkState(structure != null, "Structure not initialized");

        return structure.areNeighbors(first, second);
    }

    @Override
    public Neighbors getNeighborsOf(Position position) {
        checkState(structure != null, "Structure not initialized");

        return structure.getNeighborsOf(position);
    }


    @Override
    public void configure(StructureDefinition structure) {
        log.debug("Configuring structure structure with '{}'", structure);
        checkState(this.structure == null, "Configuring already configured structure");

        this.structure = structureCreator.createFor(structure);
    }

}
