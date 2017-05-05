package org.hage.platform.node.lifecycle.construct;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.node.lifecycle.LifecycleAction;
import org.hage.platform.node.lifecycle.LifecycleEvent;
import org.hage.platform.node.lifecycle.LifecycleState;
import org.hage.platform.node.lifecycle.LifecycleStateChangedEvent;

import static java.lang.String.format;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class TransitionDescriptor {

    private static final TransitionDescriptor NULL_TRANSITION = new TransitionDescriptor(
        new EmptyLifecycleAction(),
        null,
        null,
        null,
        true
    );

    @Getter
    private final LifecycleAction lifecycleAction;
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

    static TransitionDescriptor transition(LifecycleState initial, LifecycleState target, LifecycleEvent event, LifecycleAction lifecycleAction) {
        return new TransitionDescriptor(lifecycleAction, event, initial, target, false);
    }

    LifecycleStateChangedEvent createEvent() {
        return new LifecycleStateChangedEvent(initial, event, target);
    }
}
