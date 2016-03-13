package org.hage.platform.component.lifecycle.remote;


import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;


public class LifecycleManagerEndpoint extends BaseRemoteEndpoint<LifecycleRemoteMessage> {

    private static final String CHANEL_NAME = "lifecycle-remote-chanel";

    @Autowired
    private LifecycleEngine lifecycleEngine;

    public LifecycleManagerEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), LifecycleRemoteMessage.class);
    }

    @Override
    protected void consumeMessage(MessageEnvelope<LifecycleRemoteMessage> envelope) {
        lifecycleEngine.performCommand(envelope.getBody().getCommand());
    }

}
