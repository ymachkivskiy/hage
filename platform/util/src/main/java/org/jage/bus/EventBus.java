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
 * Created: 2013-09-13
 * $Id$
 */

package org.jage.bus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import static com.google.common.base.Objects.toStringHelper;

@ThreadSafe
public class EventBus {

	private static final Logger log = LoggerFactory.getLogger(EventBus.class);

	@Nonnull private final com.google.common.eventbus.EventBus eventBus;

	public EventBus(@Nonnull final String name) {
		this(name, false);
	}

	public EventBus(@Nonnull final String name, final boolean synchronous) {
		if (synchronous) {
			eventBus = new com.google.common.eventbus.EventBus(requireNonNull(name));
		} else {
			final ExecutorService pool = Executors.newCachedThreadPool(
					new ThreadFactoryBuilder().setNameFormat(requireNonNull(name) + "-%d").build());
			eventBus = new AsyncEventBus(requireNonNull(name), pool);
		}
	}

	/**
	 * Registers all subscriber methods on {@code object} to receive events.
	 * Subscriber methods are selected and classified using this EventBus's
	 * {@link com.google.common.eventbus.SubscriberFindingStrategy}; the default strategy is the
	 * {@link com.google.common.eventbus.AnnotatedSubscriberFinder}.
	 *
	 * @param object
	 * 		object whose subscriber methods should be registered.
	 */
	public void register(@Nonnull final Object object) {
		log.debug("Register {}.", object);

		eventBus.register(object);
	}

	/**
	 * Posts an event to all registered handlers.  This method will return
	 * successfully after the event has been posted to all handlers, and
	 * regardless of any exceptions thrown by handlers.
	 *
	 * <p>If no handlers have been subscribed for {@code event}'s class, and
	 * {@code event} is not already a {@link com.google.common.eventbus.DeadEvent}, it will be wrapped in a
	 * DeadEvent and reposted.
	 *
	 * @param event
	 * 		event to post.
	 */
	public void post(@Nonnull final Object event) {
		log.debug("Post {}.", event);

		eventBus.post(event);
	}

	/**
	 * Unregisters all handler methods on a registered {@code object}.
	 *
	 * @param object
	 * 		object whose handler methods should be unregistered.
	 *
	 * @throws IllegalArgumentException
	 * 		if the object was not previously registered.
	 */
	public void unregister(@Nonnull final Object object) {
		log.debug("Unregister {}.", object);

		eventBus.unregister(object);
	}

	@Override public String toString() {
		return toStringHelper(this).addValue(eventBus).toString();
	}
}
