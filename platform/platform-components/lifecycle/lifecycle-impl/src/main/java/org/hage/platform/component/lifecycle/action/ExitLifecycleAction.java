package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.pico.visitor.StatefulComponentFinisher;
import org.hage.platform.component.provider.IMutableComponentInstanceProvider;
import org.picocontainer.PicoContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExitLifecycleAction implements LifecycleAction {

    @Autowired
    private IMutableComponentInstanceProvider instanceProvider;

    @Override
    public void execute() {
        log.debug("Node is terminating.");
        final long start = System.nanoTime();

        // Tears down in the whole hierarchy (similar to AGE-163). Can be removed when some @PreDestroy are
        // introduced or component stopping is supported at container level.
        if (instanceProvider instanceof PicoContainer) {
            ((PicoContainer) instanceProvider).accept(new StatefulComponentFinisher());
        } else {
            // fallback for other potential implementations
            final Collection<IStatefulComponent> statefulComponents =
                instanceProvider.getInstances(IStatefulComponent.class);
            if (statefulComponents != null) {
                for (final IStatefulComponent statefulComponent : statefulComponents) {
                    try {
                        statefulComponent.finish();
                    } catch (final ComponentException e) {
                        log.error("Exception during the teardown.", e);
                    }
                }
            }
        }
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
