package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.ExecutionCore;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PRIVATE;

@SingletonComponent
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ErrorLifecycleAction implements LifecycleAction {

    @Autowired
    private ExecutionCore executionCore;

    @Override
    public void execute() {
        log.error("Node failed.");

        executionCore.stop();

        log.info("If you are running the node from the console, press Ctrl-C to exit.");
    }

}
