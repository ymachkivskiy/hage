package org.jage.lifecycle.communication;


import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.service.consume.MessageConsumer;
import org.jage.lifecycle.DefaultLifecycleManager;
import org.jage.lifecycle.LifecycleMessage;
import org.springframework.beans.factory.annotation.Autowired;


public class LifecycleManagerRemoteChanel extends BaseRemoteChanel<LifecycleMessage> {

    @Autowired
    private DefaultLifecycleManager lifecycleManager;

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
