package org.hage.platform.component.execution.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.event.CoreConfiguredEvent;
import org.hage.platform.component.execution.event.CoreStoppedEvent;
import org.hage.platform.component.execution.phase.PhasesPostProcessor;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.List;

import static com.google.common.base.Stopwatch.createUnstarted;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Order(2000)
@SingletonComponent
public class SimulationDataMonitor implements EventSubscriber, PhasesPostProcessor {

    private final Stopwatch stopwatch = createUnstarted();
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private NodeDynamicExecutionMonitor executionMonitor;

    @SuppressWarnings("unused")
    @Subscribe
    private void onCoreConfigured(CoreConfiguredEvent coreConfiguredEvent) {
        log.info("Node is ready to simulation");
        stopwatch.reset();
        stopwatch.start();
    }

    @SuppressWarnings("unused")
    @Subscribe
    private void onCoreStopped(CoreStoppedEvent coreStoppedEvent) {
        stopwatch.stop();

        log.info("Simulation execution time milliseconds: {}", stopwatch.elapsed(MILLISECONDS));
    }

    @Override
    public void afterAllPhasesExecuted(long stepNumber) {

        if (log.isInfoEnabled()) {

            DynamicExecutionInfo dynamicExecutionInfo = executionMonitor.provideExecutionInfo();

            StepSimulationInfo stepSimulationInfo = new StepSimulationInfo(
                stepNumber,
                stopwatch.elapsed(MILLISECONDS),
                summaryInfo(dynamicExecutionInfo.getUnitAgentsNumberInfos()),
                dynamicExecutionInfo.getExecutionTimeStats()
            );

            log.info("{}", stepSimulationInfo.toJson());
        }

    }


    private AgentsInfo summaryInfo(List<UnitAgentsNumberInfo> allInfos) {
        return allInfos.stream()
            .map(UnitAgentsNumberInfo::getAgentsInfo)
            .reduce(new AgentsInfo(0, 0, 0), AgentsInfo::mergeWith);
    }

    @Data
    private static class StepSimulationInfo {
        private final long step;
        private final long simulationDurationMillis;
        private final AgentsInfo agentsInfo;
        private final StepExecutionDurationInfo stepExecutionDurationInfo;


        public String toJson() {
            return new StringBuilder().append("{")
                .append("\"step\":").append(step).append(",")
                .append("\"simulationDurationMillis\":").append(simulationDurationMillis).append(",")
                .append("\"agentsInfo\":").append(agentsInfo.toJson()).append(",")
                .append("\"stepExecutionInfo\":").append(stepExecutionDurationInfo.toJson())
                .append("}")
                .toString();
        }
    }
}
