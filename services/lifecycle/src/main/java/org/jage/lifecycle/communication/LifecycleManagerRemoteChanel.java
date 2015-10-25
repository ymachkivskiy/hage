package org.jage.lifecycle.communication;


import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.consume.ConversationMessageConsumer;
import org.jage.communication.message.consume.MessageConsumer;
import org.jage.lifecycle.DefaultLifecycleManager;
import org.jage.lifecycle.LifecycleMessage;
import org.springframework.beans.factory.annotation.Autowired;


public class LifecycleManagerRemoteChanel extends BaseRemoteChanel<LifecycleMessage> {

    public static final String SERVICE_NAME = "LifecycleManager";

    @Autowired
    private DefaultLifecycleManager lifecycleManager;

    protected LifecycleManagerRemoteChanel() {
        super(SERVICE_NAME);
    }

    @Override
    protected void postInit() {
        registerMessageConsumer(new PerformCommandMessageConsumer());
    }

    private class PerformCommandMessageConsumer implements MessageConsumer<LifecycleMessage> {
        @Override
        public void consumeMessage(LifecycleMessage lifecycleMessage) {
            lifecycleManager.performCommand(lifecycleMessage.getCommand());
        }
    }
}
