package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class CoreStoppedLifecycleAction implements LifecycleAction {

    @Override
    public void execute() {
        log.debug("ExecutionCore has stopped.");
    }
}
