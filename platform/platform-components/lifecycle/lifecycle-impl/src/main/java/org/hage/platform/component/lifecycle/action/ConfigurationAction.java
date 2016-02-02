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
public class ConfigurationAction implements Action {

    @Autowired
    private CoreComponent coreComponent;

    @Override
    public void execute() {
        log.info("Configuring the computation.");

        coreComponent.configure();

        log.info("Node is configured.");
    }


}
