package org.hage.example.agent;

import lombok.extern.slf4j.Slf4j;
import org.hage.example.SomeFooComponent;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

@Slf4j
public class LightAgent implements Agent {

    @Inject
    private SomeFooComponent component;

    private int age = 0;

    @Override
    public void step(AgentManageContext context) {
        String agentUid = context.queryAddress().getFriendlyIdentifier();

        log.info("\nI am {}. My age is {}\nI leave in {} and I am surrounded by {}\nMy current neighbors are {}",
            agentUid,
            age,
            context.queryLocalUnit().getFriendlyIdentifier(),
            context.querySurroundingUnits().getAll().stream().map(UnitAddress::getFriendlyIdentifier).collect(toList()),
            context.queryOtherLocalAgents()
        );



        component.processMessage("hello from " + agentUid);

        try {
            if (age > 3) {
                context.newAgent(HeavyAgent.class, hagent -> hagent.setAge(age - 2));
            }
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        age++;
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
