package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.component.structure.connections.StructureCreationStrategy;
import org.hage.platform.component.structure.connections.StructureDefinition;

import static org.hage.platform.component.structure.connections.StructureType.FULL_GRID;

public class GridStructureCreationStrategy implements StructureCreationStrategy {

    @Override
    public boolean isApplicableFor(StructureDefinition structure) {
        return structure.getStructureType() == FULL_GRID;
    }

    @Override
    public Structure createUsingDefinition(StructureDefinition structure) {
        GridDefinition def = structure.getGridDefinition();
        return new GridStructure(def.getGridNeighborhoodType(), def.getDimensions(), def.getBoundaryConditions());
    }

}
