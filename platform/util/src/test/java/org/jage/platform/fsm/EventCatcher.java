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
 * Created: 2012-07-27
 * $Id$
 */

package org.jage.platform.fsm;

import java.util.List;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

import static com.google.common.collect.Lists.newArrayList;

/**
 * A subscriber that catches all FSM events.
 * 
 * @author AGH AgE Team
 */
public final class EventCatcher {

	private final List<Object> caughtEvents = newArrayList();

	private final List<Object> deadEvents = newArrayList();

	/**
	 * Catches StateChangedEvent.
	 * 
	 * @param event
	 *            an event to catch.
	 */
	@Subscribe
	public void catchEvent(final StateChangedEvent<?, ?> event) {
		caughtEvents.add(event);
	}

	/**
	 * Catches unexpected events.
	 * 
	 * @param event
	 *            an event to catch.
	 */
	@Subscribe
	public void catchDeadEvent(final DeadEvent event) {
		deadEvents.add(event);
	}

	/**
	 * @return caught expected events.
	 */
	public List<Object> getCaughtEvents() {
		return caughtEvents;
	}

	/**
	 * @return caught unexpected events.
	 */
	public List<Object> getDeadEvents() {
		return deadEvents;
	}
}
