package org.hage.platform.component.lifecycle.api;

import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.hage.platform.component.lifecycle.LifecycleStateMachine;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


public abstract class BaseLifecycleEngine implements LifecycleEngine {

    @Autowired
    private BeanFactory beanFactory;

    private LifecycleStateMachine engineService;

    @PostConstruct
    private void postConstruct() {

        LifecycleStateMachineBuilder builder = beanFactory.getBean(LifecycleStateMachineBuilder.class);

        performLifecycleInitialization(builder);

        engineService = builder.build();

    }

    protected final LifecycleStateMachine getStateMachine() {
        return engineService;
    }

    protected abstract void performLifecycleInitialization(LifecycleStateMachineBuilder lifecycleBuilder);

}
