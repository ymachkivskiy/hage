package org.jage.workplace;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.EventBus;
import org.jage.query.CollectionQuery;
import org.jage.services.core.CoreComponentEvent;
import org.jage.workplace.manager.WorkplaceManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.jage.query.ValueFilters.lessThan;
import static org.jage.query.ValueSelectors.field;

@Slf4j
public class FixedStepCountStopCondition
        implements IStopCondition { //TODO : re-implement and extract some base class with event posting etc.

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

    @Subscribe
    public void onCoreComponentEvent(@Nonnull final CoreComponentEvent event) {
        log.debug("Event: {}.", event);
        switch(event.getType()) {
            case STARTING:
                future = executor.scheduleWithFixedDelay(observer, 100, 100, TimeUnit.MILLISECONDS);
                break;
            case STOPPED:
                future.cancel(true);
                break;
        }

    }

    @Override
    public void init() {
        eventBus.register(this);
    }

    @Override
    public boolean finish() {
        executor.shutdown();
        return true;
    }

    private boolean shouldStop() {
        final CollectionQuery<Workplace, Long> query = new CollectionQuery<>(SimpleWorkplace.class);

        final Collection<Long> results = query.matching("step", lessThan(stepCount))
                .select(field("step")).execute(workplaceManager.getLocalWorkplaces());
//        log.info("Q={}", results);
        return results.isEmpty();
    }

}
