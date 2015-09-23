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
 * Created: 2012-02-08
 * $Id$
 */

package org.jage.communication.message;


import org.jage.address.Address;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.Selectors;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.jage.address.selector.Selectors.parentOf;
import static org.jage.address.selector.Selectors.singleAddress;


/**
 * DefaultMessage-related utilities.
 *
 * @author AGH AgE Team
 */
@Immutable
public final class Messages {

    private Messages() {
    }

    /**
     * Returns the address of the sender of the provided message.
     *
     * @param message a message to extract receiver from.
     * @param <A>     a type of addresses.
     * @return An extracted sender.
     */
    public static <A extends Address> A getSenderAddress(final Message<A, ?> message) {
        return checkNotNull(message).getHeader().getSenderAddress();
    }

    /**
     * Returns the payload carried by a message if it is of a given type. Otherwise, it throws an exception.
     *
     * @param message a message.
     * @param klass   a supposed class of the payload.
     * @param <T>     a type of the payload.
     * @return the message payload.
     * @throws IllegalArgumentException if the payload is of incorrect type.
     */
    public static <T extends Serializable> T getPayloadOfTypeOrThrow(
            final Message<? extends Address, ? super T> message, final Class<T> klass) {
        checkNotNull(message);
        checkNotNull(klass);

        final Serializable payload = checkNotNull(message.getPayload(), "Payload is null.");

        checkArgument(
                klass.isAssignableFrom(payload.getClass()),
                format("DefaultMessage payload has incorrect type. %s was expected but %s was received.", klass.getClass(),
                       payload.getClass()));
        return klass.cast(payload);
    }

    /**
     * Creates a new message that selects all components with the same name as provided from the distributed
     * environment.
     *
     * @param senderAddress the address of the sender.
     * @param payload       the payload.
     * @param <A>           a type of the address.
     * @param <P>           a type of the payload.
     * @return a new message.
     */
    public static <A extends Address, P extends Serializable> Message<A, P> newBroadcastMessage(
            final A senderAddress, final P payload) {
        final Header<A> header = SimpleHeader.create(senderAddress, Selectors.<A> allAddresses());
        return SimpleMessage.create(header, payload);
    }

    /**
     * Creates a new unicast message.
     *
     * @param from    the address of the sender.
     * @param to      the address of the receiver.
     * @param payload the payload.
     * @param <A>     a type of the address.
     * @param <P>     a type of the payload.
     * @return a new message.
     */
    public static <A extends Address, P extends Serializable> Message<A, P> newUnicastMessage(final A from,
            final A to, @Nullable final P payload) {
        final Header<A> header = SimpleHeader.create(from, singleAddress(to));
        return SimpleMessage.create(header, payload);
    }

    /**
     * Creates a new unicast message.
     *
     * @param from    the address of the sender.
     * @param payload the payload.
     * @param <A>     a type of the address.
     * @param <P>     a type of the payload.
     * @return a new message.
     */
    public static <A extends AgentAddress, P extends Serializable> Message<AgentAddress, P> newMessageToParent(
            final A from, @Nullable final P payload) {
        final SimpleHeader<AgentAddress> header = SimpleHeader.create(from, parentOf(from));
        return SimpleMessage.create(header, payload);
    }
}
