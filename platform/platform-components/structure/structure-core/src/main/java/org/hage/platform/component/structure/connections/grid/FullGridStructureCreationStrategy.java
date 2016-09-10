package org.hage.platform.component.structure.connections.grid;

import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.component.structure.connections.StructureCreationStrategy;
import org.hage.platform.component.structure.StructureDefinition;
import org.hage.platform.component.structure.grid.GridDefinition;

import static org.hage.platform.component.structure.StructureType.FULL_GRID;

public class FullGridStructureCreationStrategy implements StructureCreationStrategy {

    @Override
    public boolean isApplicableFor(StructureDefinition structure) {
        return structure.getStructureType() == FULL_GRID;
    }

    @Override
    public Structure createUsingDefinition(StructureDefinition structure) {
        GridDefinition def = structure.getGridDefinition();
        return new FullGridStructure(def.getNeighborhoodType(), def.getDimensions(), def.getGridBoundaryConditions());
    }

}
