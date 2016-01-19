package org.hage.platform.component.services.core;


import org.hage.platform.util.bus.Event;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


@Immutable
public class CoreComponentEvent implements Event {

    @Nonnull
    private final Type type;


    public CoreComponentEvent(@Nonnull final Type type) {
        this.type = checkNotNull(type);
    }

    @Nonnull
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(type).toString();
    }

    public enum Type {
        /**
         * The event sent when the core component has been successfully configured.
         */
        CONFIGURED,
        /**
         * The event sent at the beginning of the {@link CoreComponent#start()} method. It notifies that the core component
         * is about to start.
         */
        STARTING,
        /**
         * The event sent at the end of the {@link CoreComponent#stop()} method. It notifies that there is no more workplaces
         * running.
         */
        STOPPED
    }
}
