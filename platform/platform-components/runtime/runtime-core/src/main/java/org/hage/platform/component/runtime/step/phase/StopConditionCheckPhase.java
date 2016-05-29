package org.hage.platform.component.runtime.step.phase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.phase.SingleRunnableStepPhase;
import org.hage.platform.component.runtime.stopcondition.StopConditionSatisfiedChecker;
import org.hage.platform.component.runtime.stopcondition.remote.ClusterStopConditionReachedNotifier;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
@Slf4j
public class StopConditionCheckPhase extends SingleRunnableStepPhase {

    @Autowired
    private ClusterStopConditionReachedNotifier conditionReachedNotifier;
    @Autowired
    private StopConditionSatisfiedChecker stopConditionSatisfiedChecker;

    @Override
    public String getPhaseName() {
        return "Stop condition check";
    }

    @Override
    protected void executePhase(long currentStep) {
        log.debug("Checking if stop condition has been satisfied");

        if (stopConditionSatisfiedChecker.stopConditionSatisfied()) {
            conditionReachedNotifier.notifyAllStopConditionReached();
        }

    }
}
