package org.hage.platform.component.runtime.step;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.calback.StepPostProcessor;
import org.hage.platform.component.synchronization.SynchPoint;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;


@Slf4j
@Order(Integer.MAX_VALUE)
@SingletonComponent
class StepEndSynchronizationPostProcessor implements StepPostProcessor {

    @Autowired
    private SynchronizationBarrier barrier;

    @Override
    public void afterStepPerformed(long stepNumber) {
        log.debug("Synchronization at the step {} end", stepNumber);
        barrier.synchronizeOnStep(new SynchPoint(stepNumber, "finalization"));
    }

}
