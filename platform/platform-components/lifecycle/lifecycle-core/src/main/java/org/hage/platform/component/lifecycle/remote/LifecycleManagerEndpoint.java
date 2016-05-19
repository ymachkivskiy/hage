package org.hage.platform.component.lifecycle.remote;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.ClusterLifecycleManager;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.A_SYNC_PAUSE;
import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.A_SYNC_START;


@Slf4j
@SingletonComponent
class LifecycleManagerEndpoint extends BaseRemoteEndpoint<LifecycleRemoteMessage> implements ClusterLifecycleManager {

    private static final String CHANEL_NAME = "lifecycle-remote-chanel";

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    public LifecycleManagerEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), LifecycleRemoteMessage.class);
    }

    @Override
    public void schedulePauseCluster() {
        log.debug("Pausing cluster");
        sendToAllAndAggregateResponse(new LifecycleRemoteMessage(A_SYNC_PAUSE), messages -> "OK");
    }

    @Override
    public void scheduleStartCluster() {
        log.debug("Starting cluster");
        sendToAll(new LifecycleRemoteMessage(A_SYNC_START));
    }

    @Override
    protected void consumeMessage(MessageEnvelope<LifecycleRemoteMessage> envelope) {
        lifecycleCommandInvoker.invokeCommand(envelope.getBody().getCommand());
    }

}
