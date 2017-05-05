package org.hage.platform.component.loadbalance.check;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.stopcondition.StopConditionSatisfiedChecker;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SingletonComponent
class StopConditionHasNotBeenSatisfiedCheckStrategy implements BalanceCheckStrategy {

    @Autowired
    private StopConditionSatisfiedChecker stopConditionChecker;

    @Override
    public boolean shouldCheckBalance() {
        boolean canPerformReBalancing = !stopConditionChecker.stopConditionSatisfied();

        log.info("Check if can re balance with stop condition is not satisfied: {}", canPerformReBalancing);

        return canPerformReBalancing;
    }

}
