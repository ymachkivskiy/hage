package org.hage.example.agent;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.List;
import java.util.Random;

@Slf4j
public class SimpleControlAgent implements ControlAgent {

    private final Random random = new Random();


    @Override
    public void step(ControlAgentManageContext ctxt) {
      log.info("I am control agent of {} and I will kill some random agent, if I want to", ctxt.queryLocalUnit().getFriendlyIdentifier());


        if (random.nextBoolean()) {
            List<AgentAddress> localAgents = ctxt.queryLocalAgentsAddresses();
            if (!localAgents.isEmpty()) {
                AgentAddress victim = localAgents.get(random.nextInt(localAgents.size()));

                log.info("I choose to remove {}", victim);

                ctxt.killAgent(victim);
            }
        }

    }

    @Override
    public void init() {
        log.info("Initialization of agent");
    }

    @Override
    public boolean finish() {
        log.info("Finishing agent");
        return false;
    }
}
