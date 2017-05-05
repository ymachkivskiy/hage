package org.hage.mocked.simdata.agent;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.mocked.simdata.state.Properties;
import org.hage.platform.node.structure.connections.RelativePosition;
import org.hage.platform.node.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Slf4j
public class LightAgent implements Agent {

//    @Inject
//    private SomeFooComponent component;
//    @Inject
//    private MigrationCheckComponent migrationCheckComponent;

    @Setter
    private int age = 1;

    private Random random = new Random();

    @Override
    public void step(AgentManageContext context) {
        String agentUid = context.queryAddress().getFriendlyIdentifier();

        log.info("\nI am {}. My age is {} (Current step is {})\nI leave in {} and I am surrounded by {}\nMy current neighbors are {}",
            agentUid,
            age,
            context.getCurrentStep(),
            context.queryLocalUnit().getFriendlyIdentifier(),
            context.querySurroundingUnits().getAll().stream().map(UnitAddress::getFriendlyIdentifier).collect(toList()),
            context.queryOtherLocalAgents()
        );


        if (Math.abs(random.nextInt()) % 193 == 0) {
            log.debug("I have found what I want!!");
            context.notifyStopConditionSatisfied();
        }

//        component.processMessage("hello from " + agentUid);

        try {
            if (age > 3) {
                context.newAgent(HeavyAgent.class, hagent -> hagent.setAge(age - 2));
            }

            ReadWriteUnitProperties readWriteUnitProperties = context.queryLocalProperties();

            if (!readWriteUnitProperties.tryUpdate(
                Properties.ALGAE,
                currAlgae -> currAlgae >= age,
                currAlgae -> currAlgae - age)) {

                log.info("I have not enough food, will try to migrate or die...=(");

                if (age % 2 == 0) {

                    RelativePosition chosenRelative = RelativePosition.ABOVE;
//                    RelativePosition chosenRelative = migrationCheckComponent.randomRelativePosition();
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

                } else {
                    log.info("That is so sad, but I must pass away...");
                    context.die();
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
