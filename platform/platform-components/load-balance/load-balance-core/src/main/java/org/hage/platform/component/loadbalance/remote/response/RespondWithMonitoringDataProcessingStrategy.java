package org.hage.platform.component.loadbalance.remote.response;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.NodeDynamicExecutionMonitor;
import org.hage.platform.component.loadbalance.remote.message.LoadBalanceData;
import org.hage.platform.component.loadbalance.remote.message.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.remote.message.LoadBalancerRemoteMessage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.loadbalance.remote.message.LoadBalanceMessageType.RQ__PROVIDE_NODE_STATS;
import static org.hage.platform.component.loadbalance.remote.message.MessageUtils.respondWithStatsMsg;

@Slf4j
@SingletonComponent
class RespondWithMonitoringDataProcessingStrategy implements ProcessingStrategy {

    @Autowired
    private NodeDynamicExecutionMonitor nodeStatsMonitor;

    @Override
    public LoadBalanceMessageType getMessageType() {
        return RQ__PROVIDE_NODE_STATS;
    }

    @Override
    public LoadBalancerRemoteMessage process(LoadBalanceData message) {
        return respondWithStatsMsg(nodeStatsMonitor.provideExecutionInfo());
    }
}
