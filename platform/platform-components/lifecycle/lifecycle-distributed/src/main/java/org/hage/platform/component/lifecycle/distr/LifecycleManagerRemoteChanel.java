package org.hage.platform.component.lifecycle.distr;


import org.hage.platform.communication.api.BaseRemoteChanel;
import org.hage.platform.communication.message.service.consume.MessageConsumer;
import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.springframework.beans.factory.annotation.Autowired;


public class LifecycleManagerRemoteChanel extends BaseRemoteChanel<LifecycleMessage> {

    @Autowired
    private LifecycleEngine lifecycleEngine;

    @Override
    protected void postInit() {
        registerMessageConsumer(new PerformCommandMessageConsumer());
    }

    private class PerformCommandMessageConsumer implements MessageConsumer<LifecycleMessage> {
        @Override
        public void consumeMessage(LifecycleMessage lifecycleMessage) {
            lifecycleEngine.performCommand(lifecycleMessage.getCommand());
        }
    }
}
