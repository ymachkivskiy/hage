package org.hage.platform.component.lifecycle.remote;


import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;


@SingletonComponent
public class LifecycleManagerEndpoint extends BaseRemoteEndpoint<LifecycleRemoteMessage> {

    private static final String CHANEL_NAME = "lifecycle-remote-chanel";

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    public LifecycleManagerEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), LifecycleRemoteMessage.class);
    }

    @Override
    protected void consumeMessage(MessageEnvelope<LifecycleRemoteMessage> envelope) {
        lifecycleCommandInvoker.invokeCommand(envelope.getBody().getCommand());
    }

}
