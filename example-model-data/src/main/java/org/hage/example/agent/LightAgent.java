package org.hage.example.agent;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.example.MigrationCheckComponent;
import org.hage.example.SomeFooComponent;
import org.hage.platform.component.structure.connections.RelativePosition;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class LightAgent implements Agent {

    @Inject
    private SomeFooComponent component;
    @Inject
    private MigrationCheckComponent migrationCheckComponent;

    @Setter
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

            if (migrationCheckComponent.shouldPerformMigrationWithAge(age)) {

                RelativePosition chosenRelative = migrationCheckComponent.randomRelativePosition();
                List<UnitAddress> located = context.querySurroundingUnits().getLocated(chosenRelative);

                log.info("\nI {} will try to migrate {} where are {}",
                    context.queryAddress().getFriendlyIdentifier(),
                    chosenRelative,
                    located);

                if (located.size() > 0) {
                    log.info("\nI {} choose to migrate to {} - migration success is {}",
                        context.queryAddress().getFriendlyIdentifier(),
                        located.get(0),
                        context.migrateTo(located.get(0))
                    );
                }

            }

            Thread.sleep(500);
        } catch (Exception e) {
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
