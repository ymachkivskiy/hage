package org.hage.platform.component.loadbalance.core.remote.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.loadbalance.core.remote.process.LoadBalanceMessageProcessor;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SingletonComponent
public class LoadBalanceEndpoint extends BaseRemoteEndpoint<LoadBalancerRemoteMessage> {

    @Autowired
    private LoadBalanceMessageProcessor messageProcessingStrategy;

    protected LoadBalanceEndpoint() {
        super(new ConnectionDescriptor("load-balancer-remote-chanel"), LoadBalancerRemoteMessage.class);
    }




    @Override
    protected void consumeMessage(MessageEnvelope<LoadBalancerRemoteMessage> envelope) {
        messageProcessingStrategy.processAndAnswer(envelope.getBody());
    }

    @Override
    protected LoadBalancerRemoteMessage consumeMessageAndRespond(MessageEnvelope<LoadBalancerRemoteMessage> envelope) {
        return messageProcessingStrategy.processAndAnswer(envelope.getBody());
    }

}
