package org.hage.platform.node.rate.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.rate.PerformanceManager;
import org.hage.platform.node.rate.cluster.ActiveClusterPerformance;
import org.hage.platform.node.rate.cluster.ClusterPerformanceManager;
import org.hage.platform.node.rate.cluster.PerformanceRate;
import org.hage.platform.node.rate.model.ComputationRatingConfig;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@SingletonComponent
@Slf4j
class ClusterPerformanceEndpoint extends BaseRemoteEndpoint<PerformanceRemoteMessage> implements ClusterPerformanceManager {

    private static final String CHANEL_NAME = "performance-remote-chanel";

    @Autowired
    private PerformanceManager performanceManager;

    public ClusterPerformanceEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), PerformanceRemoteMessage.class);
    }

    @Override
    public ActiveClusterPerformance getNodePerformances(Set<NodeAddress> addresses, ComputationRatingConfig ratingConfig) {
        log.debug("Get performances for nodes '{}'", addresses);
        return sendToAndAggregateResponses(PerformanceRemoteMessage.rateRequestMessage(ratingConfig), this::aggregateClusterPerformance, addresses);
    }

    @Override
    protected PerformanceRemoteMessage consumeMessageAndRespond(MessageEnvelope<PerformanceRemoteMessage> envelope) {
        log.debug("Got request for local performance");
        ComputationRatingConfig ratingConfig = envelope.getBody().getRatingConfig();
        return PerformanceRemoteMessage.rateResponseMessage(performanceManager.measurePerformanceUsing(ratingConfig));
    }

    private ActiveClusterPerformance aggregateClusterPerformance(List<MessageEnvelope<PerformanceRemoteMessage>> messages) {

        Map<NodeAddress, PerformanceRate> clusterRateMap = messages.stream()
            .collect(toMap(
                MessageEnvelope::getOrigin,
                remoteMessage -> remoteMessage.getBody().getPerformanceRate()
            ));

        return new ActiveClusterPerformance(clusterRateMap);
    }

}
