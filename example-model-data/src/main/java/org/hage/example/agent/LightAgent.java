package org.hage.example.agent;

import lombok.extern.slf4j.Slf4j;
import org.hage.example.SomeFooComponent;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import javax.inject.Inject;

@Slf4j
public class LightAgent implements Agent {

    @Inject
    private SomeFooComponent component;

    @Override
    public void step(AgentManageContext context) {
        log.info("agent {} perform step", context.queryAddress().getUniqueIdentifier());

        component.processMessage("hello from " + context.queryAddress().getUniqueIdentifier());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init() {

    }

    @Override
    public boolean finish() {
        return false;
    }
}
