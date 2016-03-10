package org.hage.platform.rate.distributed;

import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.rate.local.measure.PerformanceRate;
import org.hage.platform.util.connection.remote.endpoint.MessageAggregator;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

class ClusterPerformanceMeasurementsAggregator implements MessageAggregator<PerformanceRemoteMessage, ActiveClusterPerformance> {

    @Override
    public ActiveClusterPerformance aggregate(Collection<MessageEnvelope<PerformanceRemoteMessage>> messages) {

        Map<NodeAddress, PerformanceRate> clusterRateMap = messages.stream()
            .collect(toMap(
                MessageEnvelope::getOrigin,
                remoteMessage -> remoteMessage.getBody().getPerformanceRate()
            ));

        return new ActiveClusterPerformance(clusterRateMap);
    }

}
