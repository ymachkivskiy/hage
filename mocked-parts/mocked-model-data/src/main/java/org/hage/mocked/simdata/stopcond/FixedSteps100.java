package org.hage.mocked.simdata.stopcond;

import org.hage.platform.simulation.runtime.stopcondition.SimulationState;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

public class FixedSteps100 implements StopCondition {
    @Override
    public boolean isSatisfiedBy(SimulationState state) {
        return state.getSimulationStepsPerformed() >= 100;
    }
}
