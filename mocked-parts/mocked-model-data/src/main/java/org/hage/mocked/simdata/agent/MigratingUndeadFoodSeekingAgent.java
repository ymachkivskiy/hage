package org.hage.mocked.simdata.agent;

import org.hage.mocked.simdata.Randomizer;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import javax.inject.Inject;
import java.util.List;

import static org.hage.mocked.simdata.state.Properties.FOOD;

public class MigratingUndeadFoodSeekingAgent implements Agent {

    private static final int veryGoodNewHomeThreshold = 750;
    private static final int foodPortion = 35;
    private static final int shouldWaitBeforeMigration = 1;

    @Inject
    private Randomizer randomizer;

    private int waitedSteps = 0;

    @Override
    public void step(AgentManageContext context) {

        boolean haveToLookNewHome = context.queryLocalProperties().tryUpdate(
            FOOD,
            foodAmount -> foodAmount >= foodPortion,
            foodAmount -> foodAmount - foodPortion
        );

        if (haveToLookNewHome) {

            if (waitedSteps >= shouldWaitBeforeMigration) {

                List<UnitAddress> goodPlacesToLive = context.queryNeighborsWithMatchingProperties(props ->
                    props.check(
                        FOOD,
                        foodAmount -> foodAmount >= veryGoodNewHomeThreshold
                    ));

                List<UnitAddress> allNeighbors = context.querySurroundingUnits().getAll();

                UnitAddress destination = randomizer.chooseRandom(goodPlacesToLive, allNeighbors);

                context.migrateTo(destination);

                waitedSteps = 0;

            } else {
                waitedSteps++;
            }

        }

    }

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }


}
