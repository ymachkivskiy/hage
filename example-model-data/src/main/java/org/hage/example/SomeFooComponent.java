package org.hage.example;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.simulation.container.Stateful;

@Slf4j
@ToString(callSuper = true)
public class SomeFooComponent implements Stateful {

    private final String componentScope;

    public SomeFooComponent(String componentScope) {
        this.componentScope = componentScope;
    }

    public void processMessage(String message) {
        log.info("Component {} received message : {}", this, message);
    }

    @Override
    public void init() {
        log.info("Initializing component {}", this);
    }

    @Override
    public boolean finish() {
        log.info("Finishing component {}", this);
        return true;
    }

}
