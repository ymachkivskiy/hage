package org.hage.platform.component.structure.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.LocalClusterNode;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.hage.util.concurrency.ReadWriteLockObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.structure.distribution.PositionAddressState.NOT_CORRECT_POSITION_ADDRESS;
import static org.hage.platform.component.structure.distribution.PositionState.ACTIVE;
import static org.hage.platform.component.structure.distribution.PositionState.NOT_ACTIVE;
import static org.hage.util.CollectionUtils.nullSafe;
import static org.hage.util.concurrency.ReadWriteLockObjectWrapper.wrap;

@Slf4j
public class DistributedPositionsAddressingRegistry implements LocalPositionsController, AddressingRegistry {

    @Autowired
    private StructureChangeRemoteBuffer structureChangeRemoteBuffer;
    @Autowired
    private Structure structure;
    @Autowired
    private LocalClusterNode localClusterNode;

    private final ReadWriteLockObjectWrapper<Map<Position, NodeAddress>> lockedRemoteAddressing = wrap(new HashMap<>());
    private final ReadWriteLockObjectWrapper<Set<Position>> lockedLocalPositions = wrap(new HashSet<>());

    @Override
    public PositionAddressState queryPositionAddressState(Position position) {
        log.debug("Query position address state for {}", position);

        if (!structure.belongsToStructure(position)) {
            log.debug("Position {} does not belong to simulation structure", position);
            return NOT_CORRECT_POSITION_ADDRESS;
        }

        NodeAddress address =
            lockedLocalPositions.read(localPositions -> localPositions.contains(position))
                ? localClusterNode.getLocalAddress()
                : lockedRemoteAddressing.read(addressingMap -> addressingMap.get(position));

        PositionAddressState positionState = new PositionAddressState(
            address == null ? NOT_ACTIVE : ACTIVE,
            address
        );

        log.debug("Position state is {}", positionState);

        return positionState;
    }

    @Override
    public List<ActivePositionAddress> queryActivePositionAddresses() {
        log.debug("Query active position addresses");

        List<ActivePositionAddress> positionAddresses = lockedRemoteAddressing.read(this::getActiveAddressesFromMap);

        log.debug("Actual position addresses are '{}'", positionAddresses);

        return positionAddresses;
    }

    @Override
    public List<Position> queryLocalActivePositions() {
        log.debug("Query local active positions");

        ArrayList<Position> localPositions = lockedLocalPositions.read(ArrayList::new);

        log.debug("Actual local active positions are {}", localPositions);

        return localPositions;
    }

    @Override
    public void activateLocally(Position position) {
        log.debug("Activating locally position {}", position);

        lockedLocalPositions.write(localPositions -> localPositions.add(position));
        structureChangeRemoteBuffer.addActivated(singletonList(position));
    }

    @Override
    public void deactivateLocally(Position position) {
        log.debug("Deactivating locally position {}", position);

        lockedLocalPositions.write(localPositions -> localPositions.remove(position));
        structureChangeRemoteBuffer.addDeactivated(singletonList(position));
    }

    void updatePositionsForNode(NodeAddress nodeAddress, Collection<Position> activatedPositions, Collection<Position> deactivatedPositions) {
        log.debug("Updating positions state for node {} : activated {} deactivated {}", nodeAddress, activatedPositions, deactivatedPositions);

        lockedRemoteAddressing.write(
            addressingMap -> {
                nullSafe(activatedPositions).forEach(activated -> addressingMap.put(activated, nodeAddress));
                nullSafe(deactivatedPositions).forEach(addressingMap::remove);
            }
        );
    }


    private List<ActivePositionAddress> getActiveAddressesFromMap(Map<Position, NodeAddress> addressingMap) {
        return addressingMap.entrySet()
            .stream()
            .map(e -> new ActivePositionAddress(e.getKey(), e.getValue()))
            .collect(toList());
    }

}
