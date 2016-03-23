package org.hage.platform.component.structure.connections.grid;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.connections.Position;

import java.util.Optional;

@RequiredArgsConstructor
public class BoundaryConditionsProcessor {

    private final Dimensions dimensions;
    private final BoundaryConditions boundaryConditions;


    public Optional<Position> shift(Position position){
        return null; // TODO: todo
    }

}
