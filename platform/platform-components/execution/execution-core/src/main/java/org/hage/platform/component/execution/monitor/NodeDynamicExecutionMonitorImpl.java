package org.hage.platform.component.execution.monitor;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@SingletonComponent
class NodeDynamicExecutionMonitorImpl implements NodeDynamicExecutionMonitor {

    private Random random = new Random();

    @Autowired
    private StepExecutionDurationInfoProvider stepExecutionDurationInfoProvider;

    @Override
    public DynamicExecutionInfo provideExecutionInfo() {
        return new DynamicExecutionInfo(
            stepExecutionDurationInfoProvider.getStepExecutionDurationInfo(),
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


}
