package org.hage.platform.component.runtime.step.phase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.phase.SingleRunnableStepPhase;
import org.hage.platform.component.runtime.stopcondition.StopConditionsChain;
import org.hage.platform.component.runtime.stopcondition.remote.StopConditionReachedNotifier;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
@Slf4j
public class StopConditionCheckPhase extends SingleRunnableStepPhase {

    @Autowired
    private StopConditionReachedNotifier conditionReachedNotifier;
    @Autowired
    private StopConditionsChain stopConditionsChain;

    @Override
    public String getPhaseName() {
        return "Stop condition check";
    }

    @Override
    protected void executePhase(long currentStep) {
        log.debug("Checking if stop condition has been satisfied");

        if (stopConditionsChain.stopConditionSatisfied()) {
            conditionReachedNotifier.notifyAllStopConditionReached();
        }

    }
}
