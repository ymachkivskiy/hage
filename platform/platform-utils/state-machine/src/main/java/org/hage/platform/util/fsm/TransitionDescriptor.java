package org.hage.platform.util.fsm;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

import static java.lang.String.format;

@RequiredArgsConstructor
class TransitionDescriptor<S extends Enum<S>, E extends Enum<E>> {

    @SuppressWarnings("unchecked")
    private static final TransitionDescriptor<?, ?> NULL_TRANSITION = new TransitionDescriptor(
        () -> {},
        null,
        null,
        null,
        true
    );

    @Getter
    private final Runnable action;
    @Getter
    private final E event;
    @Getter
    private final S initial;
    @Getter
    private final S target;
    @Getter
    private final boolean isNull;

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <V extends Enum<V>, Z extends Enum<Z>> TransitionDescriptor<V, Z> nullTransition() {
        return (TransitionDescriptor<V, Z>) NULL_TRANSITION;
    }

    public static <V extends Enum<V>, Z extends Enum<Z>> TransitionDescriptor<V, Z> transition(V initial, V target, Z event, Runnable action) {
        return new TransitionDescriptor<>(action, event, initial, target, false);
    }

    @Override
    public String toString() {
        return format("(%s-[%s]->%s)", initial, event, target);
    }
}
