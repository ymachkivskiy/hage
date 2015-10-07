package org.jage.lifecycle.communication;


import org.jage.communication.common.AbstractRemoteChanel;
import org.jage.lifecycle.DefaultLifecycleManager;
import org.jage.lifecycle.LifecycleMessage;
import org.springframework.beans.factory.annotation.Autowired;


public class LifecycleManagerRemoteChanel extends AbstractRemoteChanel<LifecycleMessage> {

    public static final String SERVICE_NAME = "LifecycleManager";

    @Autowired
    private DefaultLifecycleManager lifecycleManager;

    protected LifecycleManagerRemoteChanel() {
        super(SERVICE_NAME);
    }

    @Override
    protected void postInit() {
        registerConsumerHandler(message -> true, message -> lifecycleManager.performCommand(message.getCommand()));
    }
}
