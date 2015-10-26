package org.jage.communication.message.service;


import com.google.common.base.Objects.ToStringHelper;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


@Immutable
public class ServiceMessage<E extends Enum<E>, T extends Serializable> implements Serializable {

    @Getter
    private final ServiceHeader<E> header;
    @Nullable
    @Getter
    private final T payload;


    public ServiceMessage(@Nonnull final ServiceHeader<E> header) {
        this(header, null);
    }

    public ServiceMessage(@Nonnull final ServiceHeader<E> header, @Nullable final T payload) {
        this.header = checkNotNull(header);
        this.payload = payload;
    }

    @Override
    public String toString() {
        final ToStringHelper helper = toStringHelper(this).add("header", getHeader());
        if (payload != null) {
            helper.add("payload-class", payload.getClass().getSimpleName());
        }
        return helper.toString();
    }
}
