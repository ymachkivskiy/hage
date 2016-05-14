package org.hage.platform.component.monitoring;

import org.hage.platform.annotation.di.SingletonComponent;

import java.util.Collections;
import java.util.List;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofNanos;

@SingletonComponent
public class DummyMonitor implements AgentsMonitor, ExecutionTimeMonitor {

    @Override
    public SummaryAgentsStats getSummaryAgentsStats() {
        return new SummaryAgentsStats(1243, 20);
    }

    @Override
    public List<UnitAgentsStats> getUnitAgentsStats() {
        //todo : NOT IMPLEMENTED
        return Collections.emptyList();
    }

    @Override
    public ExecutionTimeStats getExecutionTimeStats() {
        return new ExecutionTimeStats(ofMillis(1234), ofNanos(10), ofMillis(13), ofMillis(783));
    }

}
