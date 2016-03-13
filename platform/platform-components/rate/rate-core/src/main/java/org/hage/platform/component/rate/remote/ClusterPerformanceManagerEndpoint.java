package org.hage.platform.component.rate.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.rate.PerformanceManager;
import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Slf4j
public class ClusterPerformanceManagerEndpoint extends BaseRemoteEndpoint<PerformanceRemoteMessage> implements ClusterPerformanceManager {

    private static final String CHANEL_NAME = "performance-remote-chanel";

    @Autowired
    private PerformanceManager performanceManager;

    public ClusterPerformanceManagerEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), PerformanceRemoteMessage.class);
    }

    @Override
    public ActiveClusterPerformance getNodePerformances(Set<NodeAddress> addresses) {
        log.debug("Get performances for nodes '{}'", addresses);
        return sendToAndAggregateResponses(PerformanceRemoteMessage.rateRequestMessage(), new ClusterPerformanceMeasurementsAggregator(), addresses);
    }

    @Override
    protected PerformanceRemoteMessage consumeMessageAndRespond(MessageEnvelope<PerformanceRemoteMessage> envelope) {
        log.debug("Got request for local performance");
        return new PerformanceRemoteMessage(performanceManager.measurePerformance());
    }

}
