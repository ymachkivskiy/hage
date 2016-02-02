package org.hage.platform.component.lifecycle;


import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.communication.message.service.ServiceHeader;
import org.hage.platform.communication.message.service.ServiceMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;


@ReturnValuesAreNonnullByDefault
@Immutable
public final class LifecycleMessage extends ServiceMessage<LifecycleCommand, Serializable> {

    private static final long serialVersionUID = 1L;

    private LifecycleMessage(final ServiceHeader<LifecycleCommand> header, @Nullable final Serializable payload) {
        super(header, payload);
    }

    @Nonnull
    public static LifecycleMessage create(@Nonnull final LifecycleCommand type,
                                          @Nullable final Serializable payload) {
        return new LifecycleMessage(ServiceHeader.create(checkNotNull(type)), payload);
    }

    @Nonnull
    public static LifecycleMessage create(@Nonnull final LifecycleCommand type) {
        return new LifecycleMessage(ServiceHeader.create(type), null);
    }

    public LifecycleCommand getCommand() {
        return getHeader().getType();
    }

}
