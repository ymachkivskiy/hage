package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.execution.ExecutionCore;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.lifecycle.LifecycleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StartLifecycleAction implements LifecycleAction {

    @Autowired
    private ExecutionCore executionCore;

    @Override
    public void execute() {
        log.info("Computation is starting.");

        try {
            executionCore.start();
        } catch (final ComponentException e) {
            throw new LifecycleException("The core component could not start.", e);
        }
    }
}
