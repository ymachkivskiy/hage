package org.hage.platform.component.execution.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.event.*;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
@Aspect
@Slf4j
class NodeExecutionCoreEventEmitterAspect {

    @Autowired
    private EventBus eventBus;

    @Pointcut("execution(void org.hage.platform.component.execution.ExecutionCore.start())")
    private void startMethod() {}

    @Pointcut("execution(void org.hage.platform.component.execution.ExecutionCore.pause())")
    private void pauseMethod() {}

    @Pointcut("execution(void org.hage.platform.component.execution.ExecutionCore.stop())")
    private void stopMethod() {}

    @Before("startMethod()")
    private void beforeStart() {
        eventBus.post(new CoreStartingEvent());
    }

    @After("startMethod()")
    private void afterStart() {
        eventBus.post(new CoreStartedEvent());
    }

    @Before("pauseMethod()")
    private void beforePause() {
        eventBus.post(new CorePausingEvent());
    }

    @After("pauseMethod()")
    private void afterPause() {
        eventBus.post(new CorePausedEvent());
    }

    @Before("stopMethod()")
    private void beforeStop() {
        eventBus.post(new CoreStoppingEvent());
    }

    @After("stopMethod()")
    private void afterStop() {
        eventBus.post(new CoreStoppedEvent());
    }

}
