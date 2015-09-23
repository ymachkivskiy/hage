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


import org.jage.address.Address;
import org.jage.address.selector.AddressSelector;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This class provides a default implementation of {@link Header}.
 *
 * @param <A> A type of sender and receiver address.
 * @author AGH AgE Team
 */
@Immutable
public class SimpleHeader<A extends Address> implements Header<A> {

    private static final long serialVersionUID = 1L;

    private final AddressSelector<A> receiverSelector;

    private final A senderAddress;

    /**
     * Constructs a new header specifying a sender and a selector for receivers.
     *
     * @param senderAddress    An address of the sender of the message.
     * @param receiverSelector A selector for selecting receivers.
     */
    private SimpleHeader(final A senderAddress, final AddressSelector<A> receiverSelector) {
        this.senderAddress = checkNotNull(senderAddress);
        this.receiverSelector = checkNotNull(receiverSelector);
    }

    /**
     * Constructs a new header specifying a sender and a selector for receivers.
     *
     * @param senderAddress    An address of the sender of the message.
     * @param receiverSelector A selector for selecting receivers.
     * @return a new header.
     */
    public static <V extends Address> SimpleHeader<V> create(final V senderAddress, final AddressSelector<V> receiverSelector) {
        return new SimpleHeader<>(senderAddress, receiverSelector);
    }

    @Override
    @Nonnull
    public A getSenderAddress() {
        return senderAddress;
    }

    @Override
    @Nonnull
    public AddressSelector<A> getReceiverSelector() {
        return receiverSelector;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("sender", senderAddress).add("receiver", receiverSelector).toString();
    }
}
