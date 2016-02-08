package org.hage.platform.component.lifecycle.distr;


import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.communication.message.service.ServiceHeader;
import org.hage.platform.communication.message.service.ServiceMessage;
import org.hage.platform.component.lifecycle.BaseLifecycleCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;


@ReturnValuesAreNonnullByDefault
@Immutable
public final class LifecycleMessage extends ServiceMessage<BaseLifecycleCommand, Serializable> {

    private LifecycleMessage(final ServiceHeader<BaseLifecycleCommand> header, @Nullable final Serializable payload) {
        super(header, payload);
    }

    @Nonnull
    public static LifecycleMessage create(@Nonnull final BaseLifecycleCommand type,
                                          @Nullable final Serializable payload) {
        return new LifecycleMessage(ServiceHeader.create(checkNotNull(type)), payload);
    }

    @Nonnull
    public static LifecycleMessage create(@Nonnull final BaseLifecycleCommand type) {
        return new LifecycleMessage(ServiceHeader.create(type), null);
    }

    public BaseLifecycleCommand getCommand() {
        return getHeader().getType();
    }

}
