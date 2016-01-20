package org.hage.platform.util.fsm;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;


class TransitionDescriptor<S extends Enum<S>, E extends Enum<E>> {

    @SuppressWarnings("unchecked")
    public static final TransitionDescriptor<?, ?> NULL = new TransitionDescriptor(null, null, null, null) {

        @Override
        boolean isNull() {
            return true;
        }

        @Override
        public String toString() {
            return format("(null)");
        }
    };
    private static final Runnable EMPTY_ACTION = new Runnable() {

        @Override
        public void run() {
            // Empty
        }
    };
    private final S initial;
    private final E event;
    @Nonnull
    private final Runnable action;
    private final S target;

    public TransitionDescriptor(@Nullable final S initial, @Nullable final E event, @Nullable final S target,
            @Nullable final Runnable action) {
        this.initial = initial;
        this.event = event;
        this.action = action != null ? action : EMPTY_ACTION;
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <V extends Enum<V>, Z extends Enum<Z>> TransitionDescriptor<V, Z> getNull() {
        return (TransitionDescriptor<V, Z>) NULL;
    }

    @Nonnull
    final Runnable getAction() {
        return action;
    }

    @Nullable
    final S getTarget() {
        return target;
    }

    @Nullable
    final S getInitial() {
        return initial;
    }

    @Nullable
    final E getEvent() {
        return event;
    }

    @SuppressWarnings("static-method")
    boolean isNull() {
        return false;
    }

    @Override
    public String toString() {
        return format("(%s-[%s]->%s)", initial, event, target);
    }
}
