package org.hage.platform.component.runtime.unit.contextadapter.location;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.RelativePosition;
import org.hage.platform.component.structure.connections.StructuralNeighborhood;
import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.component.structure.distribution.AddressingRegistry;
import org.hage.platform.component.structure.distribution.PositionAddressState;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.runtime.unit.contextadapter.location.AgentsUnitAddress.offlineAddress;
import static org.hage.platform.component.runtime.unit.contextadapter.location.AgentsUnitAddress.onlineAddress;

@Slf4j
@SingletonComponent
class NeighborsResolver {

    @Autowired
    private Structure structure;
    @Autowired
    private AddressingRegistry addressingRegistry;

    public Neighbors resolveForPosition(Position position) {
        log.debug("Resolve neighbors for position {}", position);

        NeighborsBuilder builder = new NeighborsBuilder();
        StructuralNeighborhood neighborhood = structure.getNeighborhoodOf(position);

        fillRelativePositions(neighborhood, builder);
        fillPositionAddressing(neighborhood, builder);

        return builder.build();
    }

    private void fillRelativePositions(StructuralNeighborhood neighborhood, NeighborsBuilder builder) {
        for (RelativePosition relativePosition : RelativePosition.values()) {
            builder.setRelativeNeighbors(relativePosition, neighborhood.getNeighborsFor(relativePosition));
        }
    }

    private void fillPositionAddressing(StructuralNeighborhood neighborhood, NeighborsBuilder builder) {

        for (Position position : neighborhood.getAllNeighbors()) {

            PositionAddressState positionAddressState = addressingRegistry.queryPositionAddressState(position);
            AgentsUnitAddress unitAddress = positionAddressState.isNotActive()
                ? offlineAddress(position)
                : onlineAddress(position, positionAddressState.getAddress());

            builder.setPositionAddress(position, unitAddress);
        }

    }

}
