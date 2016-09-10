package org.hage.mocked.simdata;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.simulation.runtime.stopcondition.SimulationState;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

@Slf4j
public class StopConditionChecker implements StopCondition {

    @Override
    public boolean isSatisfiedBy(SimulationState state) {
        log.info("Checking state {}", state);
        return state.getSimulationDuration().toMinutes() > 5 || state.getSimulationStepsPerformed() >= 20;
    }

}
