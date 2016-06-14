package org.hage.platform.component.execution.monitor;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.structure.Position;

import java.util.Random;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofNanos;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@SingletonComponent
public class DummyNodeDynamicExecutionMonitor implements NodeDynamicExecutionMonitor {

    private Random random = new Random();


    @Override
    public DynamicExecutionInfo provideExecutionInfo() {
        return new DynamicExecutionInfo(dummyExecutionTimeStats(),
            range(0, random.nextInt(15) + 1)
                .mapToObj(i -> randomUnitStats())
                .collect(toList())
        );
    }


    private UnitSpecificAgentsStats randomUnitStats() {
        return new UnitSpecificAgentsStats(randomPosition(), random.nextInt(150));
    }

    private Position randomPosition() {
        return Position.position(random.nextInt(100), random.nextInt(100), random.nextInt(100));
    }

    private ExecutionTimeStats dummyExecutionTimeStats() {
        return new ExecutionTimeStats(ofMillis(1234), ofNanos(10), ofMillis(random.nextInt(100) + 1), ofMillis(random.nextInt(10000) + 1));
    }

}
