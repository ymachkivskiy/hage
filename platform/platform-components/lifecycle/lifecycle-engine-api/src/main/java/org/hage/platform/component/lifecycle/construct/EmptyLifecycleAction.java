package org.hage.platform.component.lifecycle.construct;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.springframework.stereotype.Component;

@HageComponent
@Slf4j
class EmptyLifecycleAction implements LifecycleAction {

    @Override
    public void execute() {
        log.warn("Empty lifecycle action body");
    }

}
