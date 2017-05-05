package org.hage.platform.node.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.lifecycle.LifecycleAction;

import static lombok.AccessLevel.PRIVATE;

@SingletonComponent
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class CoreStoppedLifecycleAction implements LifecycleAction {

    @Override
    public void execute() {
        log.debug("ExecutionCore has stopped.");
    }
}
