package org.hage.platform.component.structure.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.structure.Position;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;


@Slf4j
@SingletonComponent
public class StructureDistributionEndpoint extends BaseRemoteEndpoint<StructureMessage> {
    private static final String CHANEL_NAME = "structure-distribution-remote-chanel";

    @Autowired
    private DistributedPositionsAddressingRegistry registry;

    protected StructureDistributionEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), StructureMessage.class);
    }

    public void updatePositions(Collection<Position> activated, Collection<Position> deactivated) {
        log.debug("Notifying about activation of positions {}", activated);
        log.debug("Notifying about deactivation of positions {}", deactivated);
        sendToAll(new StructureMessage(activated, deactivated));
    }

    @Override
    protected void consumeMessage(MessageEnvelope<StructureMessage> envelope) {
        if (!envelope.isLocalMessage()) {
            StructureMessage msg = envelope.getBody();
            registry.updatePositionsForNode(envelope.getOrigin(), msg.getActivatedPositions(), msg.getDeactivatedPositions());
        }
    }

}
