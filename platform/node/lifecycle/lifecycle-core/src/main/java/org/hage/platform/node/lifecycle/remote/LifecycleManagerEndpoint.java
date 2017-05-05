package org.hage.platform.node.lifecycle.remote;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.lifecycle.ClusterResumer;
import org.hage.platform.node.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.hage.platform.node.lifecycle.BaseLifecycleCommand;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@SingletonComponent
class LifecycleManagerEndpoint extends BaseRemoteEndpoint<LifecycleRemoteMessage> implements ClusterResumer {

    private static final String CHANEL_NAME = "lifecycle-remote-chanel";

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    public LifecycleManagerEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), LifecycleRemoteMessage.class);
    }

    @Override
    public void resumeAfterReBalance() {
        log.info("Starting cluster after re-balancing");
        sendToAllAndAggregateResponse(new LifecycleRemoteMessage(BaseLifecycleCommand.ASYNC__RESUME_AFTER_RE_BALANCE), messages -> "OK");
    }

    @Override
    protected final void consumeMessage(MessageEnvelope<LifecycleRemoteMessage> envelope) {
        lifecycleCommandInvoker.invokeCommand(envelope.getBody().getCommand());
    }

    @Override
    protected final LifecycleRemoteMessage consumeMessageAndRespond(MessageEnvelope<LifecycleRemoteMessage> envelope) {
        lifecycleCommandInvoker.invokeCommand(envelope.getBody().getCommand());
        return envelope.getBody();
    }

}
