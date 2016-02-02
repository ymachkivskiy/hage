package org.hage.platform.component.lifecycle.api;

import org.hage.platform.component.lifecycle.Action;
import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.hage.platform.component.lifecycle.LifecycleStateMachine;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


public abstract class BaseLifecycleEngine implements LifecycleEngine {

    @Autowired
    private BeanFactory beanFactory;

    private LifecycleStateMachine engineService;

    @PostConstruct
    private void postConstruct() {

        LifecycleStateMachineBuilder builder = new LifecycleStateMachineBuilder();

        performLifecycleInitialization(builder);

        builder.withEventBus(beanFactory.getBean(EventBus.class));

        engineService = builder.build();
    }


    protected final Action getActionInstanceByClass(Class<? extends Action> actionClass) {
        return beanFactory.getBean(actionClass);
    }

    protected final LifecycleStateMachine getStateMachine() {
        return engineService;
    }


    protected abstract void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder);

}
