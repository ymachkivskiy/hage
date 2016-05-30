package org.hage.platform.component.monitoring;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.structure.Position;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofNanos;
import static java.util.Arrays.asList;

@SingletonComponent
public class DummyMonitor implements NodeDynamicStatsMonitor {

    @Override
    public DynamicStats provideStats() {
        return new DynamicStats(dummyExecutionTimeStats(), asList(
            new UnitSpecificAgentsStats(Position.position(3, 4, 2), 34),
            new UnitSpecificAgentsStats(Position.position(1, 4, 2), 14),
            new UnitSpecificAgentsStats(Position.position(1, 2, 2), 51)
        ));
    }

    private ExecutionTimeStats dummyExecutionTimeStats() {
        return new ExecutionTimeStats(ofMillis(1234), ofNanos(10), ofMillis(13), ofMillis(783));
    }

}
