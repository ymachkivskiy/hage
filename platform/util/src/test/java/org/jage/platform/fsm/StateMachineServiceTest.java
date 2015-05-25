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
 * Created: 2012-07-31
 * $Id$
 */

package org.jage.platform.fsm;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the {@link StateMachineService} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class StateMachineServiceTest {

	private enum State {
		INITIAL, SECOND, THIRD, FOURTH, FAIL, TERMINAL;
	}

	private enum Event {
		A, B, C, D, E, ERROR;
	}

	// XXX: Explicit types because of javac bug
	// http://stackoverflow.com/questions/2858799/generics-compiles-and-runs-in-eclipse-but-doesnt-compile-in-javac
	// May be removed after dropping Java 6
	private final StateMachineServiceBuilder<State, Event> builder = StateMachineServiceBuilder.<State, Event> create();

	private StateMachineService<State, Event> service;

	@Before
	public void setUp() {
		builder.states(State.class).events(Event.class);
		builder.startWith(State.INITIAL);
		builder.terminateIn(State.TERMINAL);
		builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
		builder.ifFailed().fire(Event.ERROR);
	}

	@Test
	public void shouldBeInCorrectInitialState() {
		// given
		service = builder.build();

		// then
		assertThat(service.getCurrentState(), is(State.INITIAL));
	}

	@Test
	public void shouldPerformBasicTransition() {
		// given
		builder.in(State.INITIAL).on(Event.A).goTo(State.SECOND).commit();
		service = builder.build();
		
		// when
		service.fireAndWaitForTransitionToComplete(Event.A);

		// then
		assertThat(service.getCurrentState(), is(State.SECOND));
	}

	@Test
	public void shouldBeTerminatedAfterTransitionToTerminalState() {
		// given
		builder.in(State.INITIAL).on(Event.A).goTo(State.TERMINAL).commit();
		service = builder.build();

		// when
		service.fireAndWaitForTransitionToComplete(Event.A);

		// then
		assertThat(service.terminated(), is(true));
		assertThat(service.getCurrentState(), is(State.TERMINAL));
	}

	@Test
	public void shouldExecuteActionOnTransition() {
		// given
		final AtomicBoolean executed = new AtomicBoolean(false);
		builder.in(State.INITIAL).on(Event.A).execute(new Runnable() {
			@Override
			public void run() {
				executed.set(true);
			}
		}).goTo(State.SECOND).commit();
		service = builder.build();

		// when
		service.fireAndWaitForTransitionToComplete(Event.A);

		// then
		assertThat(service.getCurrentState(), is(State.SECOND));
		assertThat(executed.get(), is(true));
	}

	/**
	 * Undefined transition should result in failure event and fail state.
	 */
	@Test
	public void shouldFailIfTransitionUndefined() throws InterruptedException {
		// given
		service = builder.build();

		// when
		service.fireAndWaitForStableState(Event.A);

		// then
		assertThat(service.getCurrentState(), is(State.FAIL));
	}

	@Test
	public void shouldIgnoreEventsWhenTerminated() throws InterruptedException {
		// given
		builder.in(State.INITIAL).on(Event.A).goTo(State.TERMINAL).commit();
		builder.in(State.TERMINAL).on(Event.B).goTo(State.SECOND).commit();
		service = builder.build();

		// when
		service.fireAndWaitForTransitionToComplete(Event.A);
		service.fireAndWaitForTransitionToComplete(Event.B);

		// then
		assertThat(service.getCurrentState(), is(State.TERMINAL));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldSendNotificationsAboutStateChanges() {
		// given
		final EventCatcher eventCatcher = new EventCatcher();
		builder.in(State.INITIAL).on(Event.A).goTo(State.SECOND).commit();
		builder.notifySynchronously();
		service = builder.build();
		service.register(eventCatcher);

		// when
		service.fireAndWaitForTransitionToComplete(Event.A);

		// then
		final List<Object> events = eventCatcher.getCaughtEvents();
		assertThat(events, hasSize(1));
		final Object event = events.get(0);
		assertThat(event, is(instanceOf(StateChangedEvent.class)));
        final StateChangedEvent<State, Event> sce = (StateChangedEvent<State, Event>)event;
		assertThat(sce.getPreviousState(), is(State.INITIAL));
		assertThat(sce.getEvent(), is(Event.A));
		assertThat(sce.getNewState(), is(State.SECOND));

		assertThat(eventCatcher.getDeadEvents(), is(empty()));
	}
}
