package org.hage.platform.component.lifecycle.api;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.lifecycle.Action;
import org.hage.platform.component.lifecycle.LifecycleEvent;
import org.hage.platform.component.lifecycle.LifecycleState;
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
    private final Action action;
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

    static TransitionDescriptor transition(LifecycleState initial, LifecycleState target, LifecycleEvent event, Action action) {
        return new TransitionDescriptor(action, event, initial, target, false);
    }

    LifecycleStateChangedEvent createEvent() {
        return new LifecycleStateChangedEvent(initial, event, target);
    }
}
