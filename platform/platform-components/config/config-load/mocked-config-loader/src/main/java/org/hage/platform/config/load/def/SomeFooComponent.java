package org.hage.platform.config.load.def;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;

@Slf4j
@ToString
public class SomeFooComponent implements IStatefulComponent {

    private final String componentScope;

    public SomeFooComponent(String componentScope) {
        this.componentScope = componentScope;
    }

    public void processMessage(String message) {
        log.info("Component {} received message : {}", this, message);
    }

    @Override
    public void init() throws ComponentException {
        log.info("Initializing component {}", this);
    }

    @Override
    public boolean finish() throws ComponentException {
        log.info("Finishing component {}", this);
        return true;
    }

}
