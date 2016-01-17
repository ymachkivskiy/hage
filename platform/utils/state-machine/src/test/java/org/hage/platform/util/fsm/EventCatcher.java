package org.hage.platform.util.fsm;


import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.hage.platform.util.bus.EventSubscriber;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


public final class EventCatcher implements EventSubscriber {

    private final List<Object> caughtEvents = newArrayList();

    private final List<Object> deadEvents = newArrayList();


    public List<Object> getCaughtEvents() {
        return caughtEvents;
    }


    public List<Object> getDeadEvents() {
        return deadEvents;
    }

    @Override
    public org.hage.platform.util.bus.EventListener getEventListener() {

        return new org.hage.platform.util.bus.EventListener() {

            @Subscribe
            public void catchEvent(final StateChangedEvent<?, ?> event) {
                caughtEvents.add(event);
            }


            @Subscribe
            public void catchDeadEvent(final DeadEvent event) {
                deadEvents.add(event);
            }

        };


    }
}
