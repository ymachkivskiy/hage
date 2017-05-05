package org.hage.platform.cluster.loadbalance.remote.response;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.loadbalance.remote.message.LoadBalanceData;
import org.hage.platform.cluster.loadbalance.remote.message.LoadBalanceMessageType;
import org.hage.platform.cluster.loadbalance.remote.message.LoadBalancerRemoteMessage;
import org.hage.platform.cluster.loadbalance.remote.message.MessageUtils;
import org.hage.platform.node.execution.monitor.NodeDynamicExecutionMonitor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SingletonComponent
class RespondWithMonitoringDataProcessingStrategy implements ProcessingStrategy {

    @Autowired
    private NodeDynamicExecutionMonitor nodeStatsMonitor;

    @Override
    public LoadBalanceMessageType getMessageType() {
        return LoadBalanceMessageType.RQ__PROVIDE_NODE_STATS;
    }

    @Override
    public LoadBalancerRemoteMessage process(LoadBalanceData message) {
        return MessageUtils.respondWithStatsMsg(nodeStatsMonitor.provideExecutionInfo());
    }
}
