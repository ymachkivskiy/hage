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

package org.hage.platform.fsm;


import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * Tests for the {@link StateMachineServiceBuilder} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class StateMachineServiceBuilderTest {

    private StateMachineServiceBuilder<State, Event> builder;

    @Before
    public void setUp() {
        builder = StateMachineServiceBuilder.create();
    }

    @Test
    public void minimalRequiredConfigurationWontFail() {
        // given
        builder.states(State.class).events(Event.class);
        builder.startWith(State.INITIAL);
        builder.terminateIn(State.TERMINAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL);

        // when
        final StateMachineService<State, Event> service = builder.build();

        // then
        assertThat(builder.getStateClass(), equalTo(State.class));
        assertThat(builder.getEventClass(), equalTo(Event.class));
        assertThat(service, is(notNullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void noStatesTypeShouldResultInException() {
        // given
        builder.events(Event.class);
        builder.startWith(State.INITIAL);
        builder.terminateIn(State.TERMINAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();

        // when
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void noEventsTypeShouldResultInException() {
        // given
        builder.states(State.class);
        builder.startWith(State.INITIAL);
        builder.terminateIn(State.TERMINAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();

        // when
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void noInitialStateShouldResultInException() {
        // given
        builder.states(State.class).events(Event.class);
        builder.terminateIn(State.TERMINAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();

        // when
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void noTerminalStateShouldResultInException() {
        // given
        builder.states(State.class).events(Event.class);
        builder.startWith(State.INITIAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();

        // when
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void noFailureBehaviorShouldResultInException() {
        // given
        builder.states(State.class).events(Event.class);
        builder.startWith(State.INITIAL);
        builder.terminateIn(State.TERMINAL);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();

        // when
        builder.build();
    }

    @Test
    public void minimalRequiredConfigurationBuildsNullTable() {
        // given
        builder.states(State.class).events(Event.class);
        builder.startWith(State.INITIAL);
        builder.terminateIn(State.TERMINAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();

        // when
        final Table<State, Event, TransitionDescriptor<State, Event>> transitions = builder.buildTransitionsTable();

        // then
        for(final Cell<State, Event, TransitionDescriptor<State, Event>> cell : transitions.cellSet()) {
            if(cell.getColumnKey().equals(Event.ERROR)) {
                assertThat(cell.getValue().isNull(), is(false));
                assertThat(cell.getValue().getTarget(), is(State.FAIL));
            } else {
                assertThat(cell.getValue().isNull(), is(true));
            }
        }
    }

    @Test
    public void transitionsHaveCorrectPreferences() {
        // given
        builder.states(State.class).events(Event.class);
        builder.startWith(State.INITIAL);
        builder.terminateIn(State.TERMINAL);
        builder.ifFailed().fire(Event.ERROR);
        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
        builder.in(State.INITIAL).on(Event.A).goTo(State.SECOND).commit();

        // when
        final Table<State, Event, TransitionDescriptor<State, Event>> transitions = builder.buildTransitionsTable();

        // then
        assertThat(transitions.get(State.INITIAL, Event.A).getTarget(), equalTo(State.SECOND));
    }

    private enum State {
        INITIAL,
        SECOND,
        THIRD,
        FOURTH,
        FAIL,
        TERMINAL
    }


    private enum Event {
        A,
        B,
        C,
        D,
        E,
        ERROR
    }
}
