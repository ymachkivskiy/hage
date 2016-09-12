package org.hage.mocked.simdata.agent;

import org.hage.mocked.simdata.Randomizer;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;

import javax.inject.Inject;
import java.util.List;

import static java.lang.Math.max;
import static org.hage.mocked.simdata.state.Properties.FOOD;

public class MigratingAgent implements Agent {

    private int age = 1;
    private int energy = 50;

    private static final int foodPortion = 15;
    private static final int migrationCost = 10;
    private static final int cycleCost = 3;
    private static final int hungerStartThreshold = 25;
    private static final int reproductionCost = 20;


    private static final int idealFoodAmountThreshold = 500;

    @Inject
    private Randomizer randomizer;

    @Override
    public void step(AgentManageContext context) {

        if (energy <= 0) {
            context.die();
        } else if (energy <= hungerStartThreshold) {

            boolean atePortion = context.queryLocalProperties().tryUpdate(
                FOOD,
                foodAmount -> foodAmount >= foodPortion,
                foodAmount -> foodAmount - foodPortion
            );

            if (atePortion) {
                energy += foodPortion / 2;
            } else if (energy <= 4 * cycleCost) {
                //should find better place to live with food
                if (energy >= migrationCost) {
                    energy -= migrationCost;

                    List<UnitAddress> idealPlaces = context.queryNeighborsWithMatchingProperties(props -> props.check(FOOD, f -> f > idealFoodAmountThreshold));
                    List<UnitAddress> allNeibours = context.querySurroundingUnits().getAll();

                    context.migrateTo(randomizer.chooseRandom(idealPlaces, allNeibours));
                }
            }
        }

        if (energy >= reproductionCost && age > 2) {

            if (age < 5) {
                context.newAgent(MigratingAgent.class, agent -> agent.energy = 30);
                energy -= reproductionCost;
            } else if (age >= 5 && age <= 15) {
                context.newAgents(MigratingAgent.class, agent -> agent.energy = 55, age);
                energy -= reproductionCost * 2;
            } else {
                context.newAgent(MigratingAgent.class, agent -> agent.energy = 100);
                energy -= reproductionCost * 3;
            }

        }


        energy -= max(age / 5, 1) * cycleCost;
        age++;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }
}
