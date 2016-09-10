package org.hage.mocked.simdata.agent;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.List;
import java.util.Random;

@Slf4j
public class SimpleControlAgent implements ControlAgent {

    private final Random random = new Random();
    private int age;

    @Override
    public void step(ControlAgentManageContext ctxt) {
        log.info("\n\n==========\nI am control agent of {} and I will kill some random agent, if I want to\n", ctxt.queryLocalUnit().getFriendlyIdentifier());


        if (random.nextBoolean()) {
            List<AgentAddress> localAgents = ctxt.queryLocalAgentsAddresses();
            if (!localAgents.isEmpty()) {
                AgentAddress victim = localAgents.get(random.nextInt(localAgents.size()));

                log.info("\nxxxxxxxxxxx  I choose to remove {} xxxxxxxxxx\n", victim);

                ctxt.killAgent(victim);
            } else {
                log.info("\n=)=) It seem like all dead, lets play God and create some new agents =)=)\n");

                ctxt.newAgents(LightAgent.class, a -> a.setAge(random.nextInt(5) + 1), random.nextInt(10) + 1);
                ctxt.newAgents(HeavyAgent.class, random.nextInt(2) + 1);
            }
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
