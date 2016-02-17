package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ErrorLifecycleAction implements LifecycleAction {

    @Override
    public void execute() {
        log.error("Node failed.");
        log.info("If you are running the node from the console, press Ctrl-C to exit.");
    }

}
