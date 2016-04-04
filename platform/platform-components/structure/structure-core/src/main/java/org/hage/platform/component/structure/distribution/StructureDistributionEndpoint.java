package org.hage.platform.component.structure.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.structure.Position;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Slf4j
@Component
public class StructureDistributionEndpoint extends BaseRemoteEndpoint<StructureMessage> {
    private static final String CHANEL_NAME = "structure-distribution-remote-chanel";

    @Autowired
    private DistributedPositionsAddressingRegistry registry;

    protected StructureDistributionEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), StructureMessage.class);
    }

    @Override
    protected void consumeMessage(MessageEnvelope<StructureMessage> envelope) {
        StructureMessage msg = envelope.getBody();

        if (!envelope.isLocalMessage()) {
            registry.updatePositionsForNode(envelope.getOrigin(), msg.getActivatedPositions(), msg.getDeactivatedPositions());
        }
    }

    public void activatePositions(Collection<Position> positions) {
        log.debug("Notifying about activation of positions {}", positions);
        sendToAll(new StructureMessage(positions, null));
    }

    public void deactivatePositions(Collection<Position> positions) {
        log.debug("Notifying about deactivation of position s{}", positions);
        sendToAll(new StructureMessage(null, positions));
    }
}
