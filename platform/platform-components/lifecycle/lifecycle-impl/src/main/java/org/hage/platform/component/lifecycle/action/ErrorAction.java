package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.Action;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorAction implements Action {

    @Override
    public void execute() {
        log.error("Node failed.");
        log.info("If you are running the node from the console, press Ctrl-C to exit.");
    }

}
