package org.hage.platform.node.runtime.step;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.phase.PhasesPostProcessor;
import org.hage.platform.cluster.synch.SynchronizationBarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import static org.hage.platform.cluster.synch.SynchPoint.stepPointSubphase;


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
