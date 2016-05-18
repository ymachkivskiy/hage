package org.hage.platform.component.loadbalance.core.remote.process;

import org.apache.commons.lang3.NotImplementedException;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.core.remote.endpoint.LoadBalancerRemoteMessage;
import org.hage.platform.component.loadbalance.core.remote.process.strategy.ProcessingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;

import static java.util.Optional.ofNullable;

@SingletonComponent
public class LoadBalanceMessageProcessor {

    private final EnumMap<LoadBalanceMessageType, ProcessingStrategy> strategyMap = new EnumMap<>(LoadBalanceMessageType.class);

    public LoadBalancerRemoteMessage processAndAnswer(LoadBalancerRemoteMessage message) {
        return resolveStrategy(message.getType()).process(message.getData());
    }

    private ProcessingStrategy resolveStrategy(LoadBalanceMessageType messageType) {
        return ofNullable(strategyMap.get(messageType))
            .orElseThrow(() -> new NotImplementedException("Strategy not implemented for message type " + messageType));
    }

    @Autowired(required = false)
    private void setProcessingStrategies(List<ProcessingStrategy> strategies) {
        for (ProcessingStrategy strategy : strategies) {
            strategyMap.put(strategy.getMessageType(), strategy);
        }
    }

}
