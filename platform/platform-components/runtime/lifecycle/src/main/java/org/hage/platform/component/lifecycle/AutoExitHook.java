package org.hage.platform.component.lifecycle;


import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.util.bus.EventBus;
import org.hage.services.core.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public final class AutoExitHook implements IStatefulComponent {

    private static final Logger log = LoggerFactory.getLogger(AutoExitHook.class);

    @Inject
    private EventBus eventBus;

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }

    @Subscribe
    public void onLifecycleStateChangedEvent(@Nonnull final LifecycleStateChangedEvent event) {
        log.debug("LifecycleStateChangedEvent: {}", event);
        if (LifecycleManager.State.STOPPED.equals(event.getNewState())) {
            eventBus.post(new ExitRequestedEvent());
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }
}
