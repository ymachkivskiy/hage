package org.hage.platform.config.load.def;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.container.exception.ComponentException;
import org.hage.platform.simulation.base.Agent;
import org.hage.platform.simulation.base.Context;

import javax.inject.Inject;

@Slf4j
public class HeavyAgent implements Agent {

    @Inject
    private SomeFooComponent component;

    @Override
    public void step(Context context) {
        log.info("agent {} perform step", context.getAgentFriendlyName());

        component.processMessage("hello from " + context.getAgentFriendlyName());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init() throws ComponentException {

    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }
}
