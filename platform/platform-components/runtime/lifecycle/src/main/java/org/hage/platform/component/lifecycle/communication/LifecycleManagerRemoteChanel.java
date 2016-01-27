package org.hage.platform.component.lifecycle.communication;


import org.hage.platform.component.lifecycle.DefaultLifecycleManager;
import org.hage.platform.component.lifecycle.LifecycleMessage;
import org.hage.platform.util.communication.api.BaseRemoteChanel;
import org.hage.platform.util.communication.message.service.consume.MessageConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
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
