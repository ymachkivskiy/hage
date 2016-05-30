package org.hage.platform.component.loadbalance.check;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.ExecutionMonitor;
import org.hage.platform.component.loadbalance.config.LoadBalanceConfig;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;

@SingletonComponent
@Slf4j
class ConjunctiveStrategiesFactory implements BalanceCheckStrategyFactory {

    @Autowired
    private ExecutionMonitor executionMonitor;
    @Autowired
    private ClusterSizeCheckStrategy clusterSizeCheckStrategy;
    @Autowired
    private StopConditionHasNotBeenSatisfiedCheckStrategy stopConditionNotSatisfiedCheckStrategy;

    @Override
    public BalanceCheckStrategy buildStrategyForConfig(LoadBalanceConfig config) {
        log.debug("Building balance check strategy for configuration {}", config);

        if (config.isEnabled()) {
            return buildCompositeForConfig(config);
        } else {
            return () -> false;
        }
    }

    private BalanceCheckStrategy buildCompositeForConfig(LoadBalanceConfig config) {
        return new ConjunctionCheckStrategyComposite(
            asList(
//                clusterSizeCheckStrategy,
                stopConditionNotSatisfiedCheckStrategy,
                buildStrategy(config)
            )
        );
    }

    private BalanceCheckStrategy buildStrategy(LoadBalanceConfig config) {

        switch (config.getMode()) {
            case AFTER_STEP_COUNT:
                return new StepNumberCheckStrategy(config.getAmount(), executionMonitor::getPerformedStepsCount);
            case AFTER_SECONDS_PASSED:
                return new ExecutionDurationCheckStrategy(config.getAmount(), executionMonitor::getExecutionDuration);
            default:
                throw new NotImplementedException("Strategy for " + config.getMode() + " not implemented");
        }

    }

}
