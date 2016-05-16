package org.hage.platform.component.runtime.stopcondition.configured;

import com.google.common.base.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.ExecutionMonitor;
import org.hage.platform.component.runtime.container.StopConditionProvider;
import org.hage.platform.component.runtime.stopcondition.StopConditionChecker;
import org.hage.platform.simulation.runtime.stopcondition.SimulationState;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static com.google.common.base.Suppliers.memoize;

@Slf4j
@SingletonComponent
@Order(0)
class ConfigurationBaseStopCondition implements StopConditionChecker {

    @Autowired
    private ExecutionMonitor executionMonitor;
    @Autowired
    private StopConditionProvider stopConditionProvider;

    private Supplier<Optional<StopCondition>> cachedStopCondition;

    @PostConstruct
    private void initCache() {
        cachedStopCondition = memoize(stopConditionProvider::getStopCondition);
    }

    @Override
    public boolean isSatisfied() {
        boolean isSatisfied = checkIfSatisfied();
        log.info("Configuration provided stop condition is satisfied {}", isSatisfied);
        return isSatisfied;
    }

    private boolean checkIfSatisfied() {
        return cachedStopCondition.get().map(this::isSatisfied).orElse(false);
    }

    private boolean isSatisfied(StopCondition sc) {

        SimulationState currentState = new SimulationState(
            executionMonitor.getCurrentStepNumber(),
            executionMonitor.getExecutionDuration());

        log.debug("Checking current execution state {}", currentState);

        return sc.isSatisfiedBy(currentState);
    }

}
