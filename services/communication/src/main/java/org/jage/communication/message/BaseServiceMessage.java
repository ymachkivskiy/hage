/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2011-07-27
 * $Id$
 */

package org.jage.communication.message;


import com.google.common.base.Objects.ToStringHelper;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This class provides a default implementation of {@link org.jage.communication.message.ServiceMessage}.
 *
 * @param <T> A type of the payload transported within this message.
 * @author AGH AgE Team
 */
@Immutable
public class BaseServiceMessage<T extends Serializable> implements ServiceMessage<T> {

    private static final long serialVersionUID = 2037459335733049533L;

    private final ServiceHeader header;

    @Nullable
    private final T payload;

    /**
     * Constructs a new message with the given header and with no payload.
     *
     * @param header A header that contains metadata for this message.
     */
    public BaseServiceMessage(@Nonnull final ServiceHeader header) {
        this(header, null);
    }

    /**
     * Constructs a new message with the given header and the payload.
     *
     * @param header  A header that contains metadata for this message.
     * @param payload A payload to transport.
     */
    public BaseServiceMessage(@Nonnull final ServiceHeader header, @Nullable final T payload) {
        this.header = checkNotNull(header);
        this.payload = payload;
    }

    /**
     * Constructs a new message with the given header and with no payload.
     *
     * @param header A header that contains metadata for this message.
     * @return a new message.
     */
    public static <Z extends Serializable> BaseServiceMessage<Z> create(@Nonnull final ServiceHeader header) {
        return create(header, null);
    }

    /**
     * Constructs a new message with the given header and the payload.
     *
     * @param header  A header that contains metadata for this message.
     * @param payload A payload to transport.
     * @return a new message.
     */
    public static <Z extends Serializable> BaseServiceMessage<Z> create(@Nonnull final ServiceHeader header,
            @Nullable final Z payload) {
        return new BaseServiceMessage<>(header, payload);
    }

    @Override
    public String toString() {
        final ToStringHelper helper = toStringHelper(this).add("header", getHeader());
        if(payload != null) {
            helper.add("payload-class", payload.getClass().getSimpleName());
        }
        return helper.toString();
    }

    @Override
    @Nonnull
    public ServiceHeader getHeader() {
        return header;
    }

    @Override
    @CheckForNull
    public T getPayload() {
        return payload;
    }
}
