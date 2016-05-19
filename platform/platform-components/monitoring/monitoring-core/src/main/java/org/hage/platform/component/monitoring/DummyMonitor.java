package org.hage.platform.component.monitoring;

import org.hage.platform.annotation.di.SingletonComponent;

import java.util.Collections;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofNanos;

@SingletonComponent
public class DummyMonitor implements NodeDynamicStatsMonitor {

    @Override
    public NodeDynamicStats provideStats() {
        return new NodeDynamicStats(dummyExecutionTimeStats(), dummySimulationStats());
    }

    private ExecutionTimeStats dummyExecutionTimeStats() {
        return new ExecutionTimeStats(ofMillis(1234), ofNanos(10), ofMillis(13), ofMillis(783));
    }

    private SummaryAgentsStats dummySimulationStats() {
        return new SummaryAgentsStats(12345, Collections.emptyList());
    }
}
