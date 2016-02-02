package org.hage.platform.component.lifecycle;


import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class LifecycleEngineEngineServiceBuilderTest {
//
//    private LifecycleEngineServiceBuilder<State, Event> builder;
//
//    @Before
//    public void setUp() {
//        builder = LifecycleEngineServiceBuilder.create();
//    }
//
//    @Test
//    public void minimalRequiredConfigurationWontFail() {
//        // given
//        builder.states(State.class).events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL);
//        builder.withEventBus(mock(EventBus.class));
//
//        // when
//        final LifecycleEngineService<State, Event> service = builder.build();
//
//        // then
//        assertThat(builder.getStateClass(), equalTo(State.class));
//        assertThat(builder.getEventClass(), equalTo(Event.class));
//        assertThat(service, is(notNullValue()));
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void noStatesTypeShouldResultInException() {
//        // given
//        builder.events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//
//        // when
//        builder.build();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void noEventsTypeShouldResultInException() {
//        // given
//        builder.states(State.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//
//        // when
//        builder.build();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void noInitialStateShouldResultInException() {
//        // given
//        builder.states(State.class).events(Event.class);
//        builder.terminateIn(State.TERMINAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//
//        // when
//        builder.build();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void noTerminalStateShouldResultInException() {
//        // given
//        builder.states(State.class).events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//
//        // when
//        builder.build();
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void noFailureBehaviorShouldResultInException() {
//        // given
//        builder.states(State.class).events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//
//        // when
//        builder.build();
//    }
//
//    @Test
//    public void minimalRequiredConfigurationBuildsNullTable() {
//        // given
//        builder.states(State.class).events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//
//        // when
//        final Table<State, Event, TransitionDescriptor<State, Event>> transitions = builder.buildTransitionsTable();
//
//        // then
//        for(final Cell<State, Event, TransitionDescriptor<State, Event>> cell : transitions.cellSet()) {
//            if(cell.getColumnKey().equals(Event.ERROR)) {
//                assertThat(cell.getValue().isNull(), is(false));
//                assertThat(cell.getValue().getTarget(), is(State.FAIL));
//            } else {
//                assertThat(cell.getValue().isNull(), is(true));
//            }
//        }
//    }
//
//    @Test
//    public void transitionsHaveCorrectPreferences() {
//        // given
//        builder.states(State.class).events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.ifFailed().fire(Event.ERROR);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//        builder.in(State.INITIAL).on(Event.A).goTo(State.SECOND).commit();
//
//        // when
//        final Table<State, Event, TransitionDescriptor<State, Event>> transitions = builder.buildTransitionsTable();
//
//        // then
//        assertThat(transitions.get(State.INITIAL, Event.A).getTarget(), equalTo(State.SECOND));
//    }
//
//    private enum State {
//        INITIAL,
//        SECOND,
//        THIRD,
//        FOURTH,
//        FAIL,
//        TERMINAL
//    }
//
//
//    private enum Event {
//        A,
//        B,
//        C,
//        D,
//        E,
//        ERROR
//    }
}