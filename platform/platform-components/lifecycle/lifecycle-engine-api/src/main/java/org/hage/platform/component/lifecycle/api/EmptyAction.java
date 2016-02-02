package org.hage.platform.component.lifecycle.api;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.Action;

@Slf4j
class EmptyAction implements Action {

    static final Action INSTANCE = new EmptyAction();

    private EmptyAction() {
    }

    @Override

    public void execute() {
        log.warn("Empty lifecycle action body");
    }

}
