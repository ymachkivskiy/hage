package org.hage.platform.component.lifecycle.communication;


import org.hage.communication.api.BaseRemoteChanel;
import org.hage.communication.message.service.consume.MessageConsumer;
import org.hage.platform.component.lifecycle.DefaultLifecycleManager;
import org.hage.platform.component.lifecycle.LifecycleMessage;
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
