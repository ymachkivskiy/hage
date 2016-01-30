package org.hage.platform.component.workplace;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.query.CollectionQuery;
import org.hage.platform.component.query.ValueFilters;
import org.hage.platform.component.services.core.CoreComponentEvent;
import org.hage.platform.component.workplace.manager.WorkplaceManager;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hage.platform.component.query.ValueSelectors.field;

@Slf4j
public class FixedStepCountStopCondition implements
        IStopCondition,
        EventSubscriber
{ //TODO : re-implement and extract some base class with event posting etc.

    private final EventListener eventListener = new PrivateEventListener();

    private static final long DEFAULT_STEP_COUNT = 2;

    private final long stepCount;

    private final AtomicBoolean alreadySatisfied = new AtomicBoolean(false);

    private final ListeningScheduledExecutorService executor = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadScheduledExecutor());

    private ScheduledFuture<?> future;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private WorkplaceManager workplaceManager;


    private Runnable observer = new Runnable() {

        @Override
        public void run() {
            if(!alreadySatisfied.get() && shouldStop()) {
                log.info("The stop condition has been satisfied.");
                alreadySatisfied.set(true);
                eventBus.post(new StopConditionFulfilledEvent());
            }
        }
    };

    /**
     * Creates a new step counting condition with the default maximum step count.
     */
    public FixedStepCountStopCondition() {
        this(DEFAULT_STEP_COUNT);
    }

    /**
     * Creates a new step counting condition with the provided maximum step count.
     *
     * @param stepCount a number of steps before this stop condition is satisfied, must be greater than zero.
     */
    public FixedStepCountStopCondition(final Long stepCount) {
        checkNotNull(stepCount);
        checkArgument(stepCount > 0,
                      "FixedStepCountStopCondition: number of steps cannot be equal or less then zero. Given value: %s.",
                      stepCount);
        this.stepCount = stepCount;
        log.info("Fixed step stop condition created with step set to: {}.", this.stepCount);
    }


    @PostConstruct
    @Override
    public void init() {
    }

    @PreDestroy
    @Override
    public boolean finish() {
        executor.shutdown();
        return true;
    }

    private boolean shouldStop() {
        final CollectionQuery<Workplace, Long> query = new CollectionQuery<>(SimpleWorkplace.class);

        final Collection<Long> results = query.matching("step", ValueFilters.lessThan(stepCount))
                .select(field("step")).execute(workplaceManager.getLocalWorkplaces());
//        log.info("Q={}", results);
        return results.isEmpty();
    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    private class PrivateEventListener implements EventListener {

        @Subscribe
        public void onCoreComponentEvent(CoreComponentEvent event) {
            log.debug("Event: {}.", event);
            switch(event.getType()) {
                case STARTING:
                    future = executor.scheduleWithFixedDelay(observer, 0, 50, TimeUnit.MILLISECONDS);
                    break;
                case STOPPED:
                    future.cancel(true);
                    break;
            }
        }

    }

}
