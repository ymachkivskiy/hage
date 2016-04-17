package org.hage.example.agent;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.example.SomeFooComponent;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import javax.inject.Inject;

@Slf4j
public class HeavyAgent implements Agent {

    @Inject
    private SomeFooComponent component;

    @Setter
    private int age = 0;

    @Override
    public void step(AgentManageContext context) {
        log.info("\nI AM HEAVY AGE OF {} agent {} perform step", age, context.queryAddress().getUniqueIdentifier());

        component.processMessage("hello from " + context.queryAddress().getUniqueIdentifier());

        if(age > 20){
            context.die();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        age++;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean finish() {
        return false;
    }
}
