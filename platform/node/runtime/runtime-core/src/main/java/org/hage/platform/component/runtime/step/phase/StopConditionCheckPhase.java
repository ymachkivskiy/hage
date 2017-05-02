package org.hage.platform.component.runtime.step.phase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.execution.phase.SingleTaskExecutionPhase;
import org.hage.platform.component.runtime.stopcondition.StopConditionSatisfiedChecker;
import org.hage.platform.component.runtime.stopcondition.remote.ClusterStopConditionReachedNotifier;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.execution.phase.ExecutionPhaseType.MAIN__CONDITION_CHECK;

@SingletonComponent
@Slf4j
public class StopConditionCheckPhase extends SingleTaskExecutionPhase {

    @Autowired
    private ClusterStopConditionReachedNotifier conditionReachedNotifier;
    @Autowired
    private StopConditionSatisfiedChecker stopConditionSatisfiedChecker;

    @Override
    public ExecutionPhaseType getType() {
        return MAIN__CONDITION_CHECK;
    }

    @Override
    protected void execute(long currentStep) {
        log.debug("Checking if stop condition has been satisfied");

        if (stopConditionSatisfiedChecker.stopConditionSatisfied()) {
            conditionReachedNotifier.notifyAllStopConditionReached();
        }

    }
}
