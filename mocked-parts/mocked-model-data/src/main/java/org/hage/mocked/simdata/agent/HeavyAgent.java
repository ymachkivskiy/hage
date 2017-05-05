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
import java.util.Optional;
import java.util.Random;

@Slf4j
public class HeavyAgent implements Agent {

//    @Inject
//    private SomeFooComponent component;

    private Random rand = new Random();

    @Setter
    private int age = 0;

    @Override
    public void step(AgentManageContext ctxt) {
        log.info("\nI AM HEAVY AGE OF {} agent {} perform step. \nI have neighbors of same type  {}", age, ctxt.queryAddress().getFriendlyIdentifier(), ctxt.queryOtherLocalAgentsOfSameType());

        if (age == 6) {
            log.info("\nI AM LUCKY alive, notifying about that miracle");
            ctxt.notifyStopConditionSatisfied();
        }

//        component.processMessage("hello from " + ctxt.queryAddress().getFriendlyIdentifier());


        if (age > 2 && rand.nextBoolean() && rand.nextBoolean()) {
            ctxt.die();
        } else {
            ReadWriteUnitProperties localProperties = ctxt.queryLocalProperties();

            log.info("\nLocal properties {}", localProperties.getValues());

            if (localProperties.check(Properties.TEMPERATURE, t -> t >= 40)) {

                log.info("\nIt is too hot for me, I will try to migrate to other place");

                Optional<UnitAddress> randomNeighbor = ctxt.querySurroundingUnits()
                    .getLocated(RelativePosition.BELOW)
                    .stream()
                    .findFirst();

                if (randomNeighbor
                    .map(ctxt::queryPropertiesOf)
                    .flatMap(belowProps -> belowProps.get(Properties.TEMPERATURE))
                    .map(t -> t < 40)
                    .orElse(false)) {

                    log.info("\nI have found good place to leave in {} (there is great temperature", randomNeighbor.get());

                    ctxt.migrateTo(randomNeighbor.get());

                } else {
                    log.info("\nI will try to find unit with matching conditions");

                    List<UnitAddress> goodUnits = ctxt.queryNeighborsWithMatchingProperties(
                        props -> props.check(Properties.TEMPERATURE, t -> t < 38) && props.check(Properties.ALGAE, a -> a >= 50)
                    );

                    goodUnits.stream().findFirst().ifPresent(
                        ua -> {
                            log.info("\nI have found!!!! {}", ua);
                            ctxt.migrateTo(ua);
                        }
                    );
                }

            }
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
