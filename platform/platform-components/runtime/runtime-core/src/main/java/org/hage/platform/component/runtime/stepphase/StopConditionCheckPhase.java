package org.hage.platform.component.runtime.stepphase;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.SingleRunnableStepPhase;
import org.hage.platform.component.runtime.stopcondition.StopConditionsChain;
import org.hage.platform.component.runtime.stopcondition.remote.StopConditionEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
@Slf4j
public class StopConditionCheckPhase extends SingleRunnableStepPhase {

    @Autowired
    private StopConditionEndpoint stopConditionEndpoint;
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
            stopConditionEndpoint.notifyAllStopConditionReached();
        }

    }
}
