package org.hage.platform.cluster.loadbalance.check;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.monitor.SimulationExecutionMonitor;
import org.hage.platform.cluster.loadbalance.config.LoadBalanceConfig;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;

@SingletonComponent
@Slf4j
class ConjunctiveStrategiesFactory implements BalanceCheckStrategyFactory {

    @Autowired
    private SimulationExecutionMonitor simulationExecutionMonitor;
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
                clusterSizeCheckStrategy,
                stopConditionNotSatisfiedCheckStrategy,
                buildStrategy(config)
            )
        );
    }

    private BalanceCheckStrategy buildStrategy(LoadBalanceConfig config) {

        switch (config.getMode()) {
            case AFTER_STEP_COUNT:
                return new StepNumberCheckStrategy(config.getAmount(), simulationExecutionMonitor::getPerformedStepsCount);
            case AFTER_SECONDS_PASSED:
                return new ExecutionDurationCheckStrategy(config.getAmount(), simulationExecutionMonitor::getSimulationExecutionDuration);
            default:
                throw new NotImplementedException("Strategy for " + config.getMode() + " not implemented");
        }

    }

}
