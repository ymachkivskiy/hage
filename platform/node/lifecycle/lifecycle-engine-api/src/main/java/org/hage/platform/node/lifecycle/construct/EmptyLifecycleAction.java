package org.hage.platform.node.lifecycle.construct;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.lifecycle.LifecycleAction;

@SingletonComponent
@Slf4j
class EmptyLifecycleAction implements LifecycleAction {

    @Override
    public void execute() {
        log.warn("Empty lifecycle action body");
    }

}
