package org.hage.platform.component.runtime.step;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.PhasesPostProcessor;
import org.hage.platform.component.synchronization.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import static org.hage.platform.component.synchronization.SynchPoint.stepPointSubphase;


@Slf4j
@Order(Integer.MAX_VALUE)
@SingletonComponent
class StepEndSynchronizationPostProcessor implements PhasesPostProcessor {

    @Autowired
    private SynchronizationBarrier barrier;

    @Override
    public void afterAllPhasesExecuted(long stepNumber) {
        log.debug("Synchronization at the end of step {}", stepNumber);
        barrier.synchronize(stepPointSubphase(stepNumber, "finalization"));
    }

}
