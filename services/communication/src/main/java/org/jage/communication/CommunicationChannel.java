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
 * Created: 2013-08-11
 * $Id$
 */

package org.jage.communication;


import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Sets.newCopyOnWriteArraySet;


/**
 * A Hazelcast-based communication channel for services of the same type on different nodes. It works in the
 * publish-subscribe model.
 * <p>
 * <p>It is mostly a wrapper that hides ITopic from Hazelcast with our own API.
 *
 * @param <T> A type of messages sent through the channel.
 */
@ThreadSafe
public class CommunicationChannel<T> {

    private static final Logger log = LoggerFactory.getLogger(CommunicationChannel.class);

    @Nonnull
    private final ITopic<T> topic;

    private final Set<MessageSubscriber<T>> subscribers = newCopyOnWriteArraySet();

    public CommunicationChannel(@Nonnull final ITopic<T> topic) {
        this.topic = topic;
        topic.addMessageListener(new Listener());
    }

    /**
     * Publishes a message in the channel.
     *
     * @param message a message to publish.
     */
    public void publish(@Nonnull final T message) {
        log.debug("Publishing {} on the channel {}.", message, this);

        topic.publish(message);
    }

    /**
     * Subscribes a listener to the channel.
     *
     * @param listener a listener that will receive future messages published in the channel.
     */
    public void subscribe(@Nonnull MessageSubscriber<T> listener) {
        log.debug("Subscribe {} to the channel {}.", listener, this);

        subscribers.add(listener);
    }

    /**
     * Unsubscribes a listener from the channel.
     *
     * @param listener a listener that should be unsubscribed.
     */
    public void unsubscribe(@Nonnull MessageSubscriber<T> listener) {
        log.debug("Unsubscribe {} from the channel {}.", listener, this);

        subscribers.remove(listener);
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("name", topic.getName()).toString();
    }

    private class Listener implements MessageListener<T> {

        @Override
        public void onMessage(@Nonnull final Message<T> message) {
            log.debug("Message {} on the channel {}.", message, this);

            final T messageObject = message.getMessageObject();
//TODO : check if it can not be optimized with executing in other threads (probably not)
            for(final MessageSubscriber<T> subscriber : subscribers) {
                subscriber.onMessage(messageObject);
            }
        }
    }
}
