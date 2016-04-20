package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.ConfigurationConsumer;
import org.hage.platform.component.simulationconfig.ConfigurationProvider;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PRIVATE;

@SingletonComponent
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ConfigurationLifecycleAction implements LifecycleAction {

    @Autowired
    private ConfigurationConsumer configurationConsumer;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private SynchronizationBarrier barrier;

    @Override
    public void execute() {
        configureLocalNode();
        waitForOther();
    }

    private void configureLocalNode() {
        log.info("Configuring the computation.");

        Configuration configuration = configurationProvider.provideConfiguration();

        configurationConsumer.acceptConfiguration(configuration);

        log.info("Node is configured.");
    }

    private void waitForOther() {
        log.info("Synchronizing with other nodes started...");

        barrier.synchronize();

        log.info("Synchronization finished");
    }


}
