package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.Action;
import org.hage.platform.component.services.core.CoreComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClearAction implements Action {

    @Autowired
    private CoreComponent coreComponent;

    @Override
    public void execute() {
        log.info("Computation configuration is being removed.");

        coreComponent.resume();
    }

}
