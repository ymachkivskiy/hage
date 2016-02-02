package org.hage.platform.component.lifecycle;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.event.LifecycleEvent;
import org.hage.platform.component.lifecycle.event.LifecycleState;
import org.hage.platform.component.lifecycle.event.LifecycleStateChangedEvent;

import static java.lang.String.format;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class TransitionDescriptor {

    private static final TransitionDescriptor NULL_TRANSITION = new TransitionDescriptor(
        () -> {
        },
        null,
        null,
        null,
        true
    );

    @Getter
    private final Runnable action;
    @Getter
    private final LifecycleEvent event;
    @Getter
    private final LifecycleState initial;
    @Getter
    private final LifecycleState target;
    @Getter
    private final boolean isNull;

    @Override
    public String toString() {
        return format("(%s-[%s]->%s)", initial, event, target);
    }

    public static TransitionDescriptor nullTransition() {
        return NULL_TRANSITION;
    }

    public static TransitionDescriptor transition(LifecycleState initial, LifecycleState target, LifecycleEvent event, Runnable action) {
        return new TransitionDescriptor(action, event, initial, target, false);
    }

    public LifecycleStateChangedEvent createEvent() {
        return new LifecycleStateChangedEvent(initial, event, target);
    }
}
