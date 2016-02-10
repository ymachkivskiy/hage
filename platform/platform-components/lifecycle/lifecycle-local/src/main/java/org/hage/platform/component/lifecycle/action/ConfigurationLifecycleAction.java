package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.ExecutionCore;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationLifecycleAction implements LifecycleAction {

    @Autowired
    private ExecutionCore executionCore;

    @Override
    public void execute() {
        log.info("Configuring the computation.");

        executionCore.configure();

        log.info("Node is configured.");
    }


}
