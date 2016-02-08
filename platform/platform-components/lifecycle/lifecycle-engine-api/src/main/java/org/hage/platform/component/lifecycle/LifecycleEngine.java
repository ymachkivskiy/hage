package org.hage.platform.component.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.construct.LifecycleStateMachineBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;

import static java.lang.Runtime.getRuntime;

@Slf4j
public class LifecycleEngine {

    @Autowired
    private BeanFactory beanFactory;
    private LifecycleInitializer lifecycleInitializer;

    private LifecycleStateMachine engineService;

    public final void start() {
        engineService.fire(lifecycleInitializer.getStartingEvent());
    }

    public final void performCommand(LifecycleCommand command) {
        command.accept(engineService);
    }

    @PostConstruct
    private void postConstruct() {

        LifecycleStateMachineBuilder builder = beanFactory.getBean(LifecycleStateMachineBuilder.class);

        lifecycleInitializer.performLifecycleInitialization(builder);

        engineService = builder.build();

        getRuntime().addShutdownHook(new ShutdownHook());
    }

    @Required
    public void setLifecycleInitializer(LifecycleInitializer lifecycleInitializer) {
        this.lifecycleInitializer = lifecycleInitializer;
    }


    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            log.debug("Shutdown hook called.");

            if (!engineService.terminated() && !engineService.isTerminating()) {
                engineService.fire(LifecycleEvent.EXIT);
                try {
                    Thread.sleep(2000); // Simple wait to let other threads terminate properly.
                } catch (final InterruptedException ignored) {
                    // Ignore
                }
            }
        }
    }

}
