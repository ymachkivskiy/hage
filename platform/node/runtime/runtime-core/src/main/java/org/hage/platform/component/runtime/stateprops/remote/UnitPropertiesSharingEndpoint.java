package org.hage.platform.component.runtime.stateprops.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.stateprops.registry.PositionUnitProperties;
import org.hage.platform.component.runtime.stateprops.registry.UnitPropertiesRefresher;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SingletonComponent
@Slf4j
class UnitPropertiesSharingEndpoint extends BaseRemoteEndpoint<UnitPropertiesMessage> implements UnitPropertiesSharer {

    private static final String CHANEL_NAME = "unit-properties-remote-chanel";

    @Autowired
    private UnitPropertiesRefresher unitPropertiesRefresher;

    protected UnitPropertiesSharingEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), UnitPropertiesMessage.class);
    }

    @Override
    public void shareUpdatedProperties(List<PositionUnitProperties> unitProperties) {
        log.debug("Share updated properties with all");
        sendToAll(new UnitPropertiesMessage(unitProperties));
    }

    @Override
    protected void consumeMessage(MessageEnvelope<UnitPropertiesMessage> envelope) {
        log.debug("Got shared properties from {}", envelope.getOrigin());
        if (!envelope.isLocalMessage()) {
            log.debug("Update properties from other node");
            unitPropertiesRefresher.updatePropertiesUsing(envelope.getBody().getPositionUnitProperties());
        }
    }

}
