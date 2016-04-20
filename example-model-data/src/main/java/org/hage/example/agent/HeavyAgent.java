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
    public void step(AgentManageContext ctxt) {
        log.info("\nI AM HEAVY AGE OF {} agent {} perform step. \nI have neighbors of same type  {}", age, ctxt.queryAddress().getFriendlyIdentifier(), ctxt.queryOtherLocalAgentsOfSameType());

        component.processMessage("hello from " + ctxt.queryAddress().getFriendlyIdentifier());

        if(age > 2){
            ctxt.die();
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
        log.info("Initialization of agent {}", this);
    }

    @Override
    public boolean finish() {
        log.info("Finishing agent {}", this);
        return false;
    }
}
