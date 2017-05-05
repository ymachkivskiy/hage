package org.hage.platform.node.structure.connections.grid;

import org.hage.platform.node.structure.connections.Structure;
import org.hage.platform.node.structure.connections.StructureCreationStrategy;
import org.hage.platform.node.structure.StructureDefinition;
import org.hage.platform.node.structure.grid.GridDefinition;
import org.hage.platform.node.structure.StructureType;

public class FullGridStructureCreationStrategy implements StructureCreationStrategy {

    @Override
    public boolean isApplicableFor(StructureDefinition structure) {
        return structure.getStructureType() == StructureType.FULL_GRID;
    }

    @Override
    public Structure createUsingDefinition(StructureDefinition structure) {
        GridDefinition def = structure.getGridDefinition();
        return new FullGridStructure(def.getNeighborhoodType(), def.getDimensions(), def.getGridBoundaryConditions());
    }

}
