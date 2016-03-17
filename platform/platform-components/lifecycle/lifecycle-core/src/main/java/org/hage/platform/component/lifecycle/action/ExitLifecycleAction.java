package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ExitLifecycleAction implements LifecycleAction {

    @Autowired
    private MutableInstanceContainer instanceProvider;

    @Override
    public void execute() {
        log.debug("Node is terminating.");
        final long start = System.nanoTime();

        instanceProvider.finishStatefulComponents();

        log.info("Node terminated.");

        if (log.isDebugEnabled()) {
            final Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();

            for (final Map.Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()) {
                final Thread thread = entry.getKey();
                if (!thread.equals(Thread.currentThread()) && thread.isAlive() && !thread.isDaemon()) {
                    log.debug("{} has not been shutdown properly.", entry.getKey());
                    for (final StackTraceElement e : entry.getValue()) {
                        log.debug("\t{}", e);
                    }
                }
            }
        }

        final long elapsedTime = System.nanoTime() - start;
        log.debug("Shutdown took {} ms.", elapsedTime / 1000000);
    }
}
