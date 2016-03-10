package org.hage.platform.rate.distributed;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.rate.local.LocalPerformanceManager;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.hage.platform.rate.distributed.PerformanceRemoteMessage.rateRequestMessage;

@Slf4j
class ClusterPerformanceManagerEndpoint extends BaseRemoteEndpoint<PerformanceRemoteMessage> implements ClusterPerformanceManager {

    private static final String CHANEL_NAME = "performance-remote-chanel";

    @Autowired
    private LocalPerformanceManager localPerformanceManager;

    public ClusterPerformanceManagerEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), PerformanceRemoteMessage.class);
    }

    @Override
    public ActiveClusterPerformance getNodePerformances(Set<NodeAddress> addresses) {
        log.debug("Get performances for nodes '{}'", addresses);
        return sendToAndAggregateResponses(rateRequestMessage(), new ClusterPerformanceMeasurementsAggregator(), addresses);
    }

    @Override
    protected PerformanceRemoteMessage consumeMessageAndRespond(MessageEnvelope<PerformanceRemoteMessage> envelope) {
        log.debug("Got request for local performance");
        return new PerformanceRemoteMessage(localPerformanceManager.getLocalPerformanceRate());
    }

}
