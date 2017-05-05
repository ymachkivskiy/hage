package org.hage.platform.node.runtime.unitmove.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.runtime.migration.UnitMigrationPerformer;
import org.hage.platform.node.runtime.unitmove.PackedUnit;
import org.hage.platform.node.runtime.unitmove.UnitDeactivationPacker;
import org.hage.platform.node.runtime.unitmove.UnitUnpackingQueue;
import org.hage.platform.node.structure.Position;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.node.runtime.unitmove.remote.UnitMoveMessage.ack;
import static org.hage.platform.node.runtime.unitmove.remote.UnitMoveMessage.move;
import static org.hage.util.CollectionUtils.nullSafe;

@SingletonComponent
@Slf4j
public class UnitMigrationPerformingEndpoint extends BaseRemoteEndpoint<UnitMoveMessage> implements UnitMigrationPerformer {

    @Autowired
    private UnitDeactivationPacker unitDeactivationPacker;
    @Autowired
    private UnitUnpackingQueue unitUnpackingQueue;

    protected UnitMigrationPerformingEndpoint() {
        super(new ConnectionDescriptor("remote-unit-move-chanel-descriptor"), UnitMoveMessage.class);
    }

    @Override
    public void migrateUnits(List<Position> unitsPositions, NodeAddress targetNode) {
        log.debug("Migrate units {} to node {}", nullSafe(unitsPositions), targetNode);

        List<PackedUnit> packedUnits = packUnits(unitsPositions);
        sendToAndWaitForResponse(move(packedUnits), targetNode);
    }

    @Override
    protected UnitMoveMessage consumeMessageAndRespond(MessageEnvelope<UnitMoveMessage> envelope) {

        UnitMoveMessage unitMoveMessage = envelope.getBody();

        if (unitMoveMessage.getType() == MessageType.SEND) {
            log.debug("Accept units from {} : {}", envelope.getOrigin(), unitMoveMessage.getPackedUnits());
            unitUnpackingQueue.scheduleUnpackAndActivation(unitMoveMessage.getPackedUnits());
        }

        return ack();
    }

    private List<PackedUnit> packUnits(List<Position> unitsPositions) {
        return unitsPositions.stream()
            .map(unitDeactivationPacker::deactivateAndPack)
            .collect(toList());
    }

}
