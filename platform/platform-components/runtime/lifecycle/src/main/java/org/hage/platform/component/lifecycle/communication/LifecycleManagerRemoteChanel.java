package org.hage.platform.component.lifecycle.communication;


import org.hage.platform.communication.api.BaseRemoteChanel;
import org.hage.platform.communication.message.service.consume.MessageConsumer;
import org.hage.platform.component.lifecycle.DefaultLifecycleEngine;
import org.hage.platform.component.lifecycle.LifecycleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LifecycleManagerRemoteChanel extends BaseRemoteChanel<LifecycleMessage> {

    @Autowired
    private DefaultLifecycleEngine lifecycleManager;

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
