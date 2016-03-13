package org.hage.platform.component.execution.core.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.simulation.structure.CellExecutionPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.EnumSet.noneOf;
import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class PhasedExecutor {

    @Autowired
    private CoreBatchExecutor coreBatchExecutor;

    private final Collection<PhasedRunnable> phasedRunnables = new HashSet<>();

    public synchronized void register(Collection<PhasedRunnable> runnables) {
        if (log.isDebugEnabled()) {
            for (PhasedRunnable runnable : runnables) {
                log.debug("Register runnable: {}", runnable);
            }
        }

        phasedRunnables.addAll(runnables);
    }

    public synchronized void unregister(Collection<PhasedRunnable> runnables) {
        if (log.isDebugEnabled()) {
            for (PhasedRunnable runnable : runnables) {
                log.debug("Unregister runnable: {}", runnable);
            }
        }

        phasedRunnables.removeAll(runnables);
    }

    public synchronized void unregisterAll() {
        log.debug("Unregister all runnables");

        phasedRunnables.clear();
    }

    public synchronized void performFullCycle(List<CellExecutionPhase> phasesInOrder) {
        log.debug("Execute full cycle for phases {}:  STARTED", phasesInOrder);

        Set<CellExecutionPhase> alreadyExecutedPhases = noneOf(CellExecutionPhase.class);

        phasesInOrder.stream()
            .filter(alreadyExecutedPhases::add)
            .forEach(this::executePhase);

        log.debug("Execute full cycle for phases {}:  FINISHED", phasesInOrder);

    }

    private void executePhase(CellExecutionPhase phase) {
        log.debug("Executing phase: {}", phase);

        List<PhasedRunnableAdapter> runnableAdapters = phasedRunnables
            .stream()
            .map(phasedRunnable -> new PhasedRunnableAdapter(phasedRunnable, phase))
            .collect(toList());

        coreBatchExecutor.executeAll(runnableAdapters);
    }

    @RequiredArgsConstructor
    private static class PhasedRunnableAdapter implements Runnable {

        private final PhasedRunnable phasedRunnable;
        private final CellExecutionPhase phase;

        @Override
        public void run() {
            phasedRunnable.runPhase(phase);
        }

    }
}
