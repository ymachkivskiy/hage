package org.hage.platform.util.fsm;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class StateMachineServiceTest {

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
        service = builder.build();
        service.getEventBus().register(eventCatcher);

        // when
        service.fireAndWaitForTransitionToComplete(Event.A);

        // then
        final List<Object> events = eventCatcher.getCaughtEvents();
        assertThat(events, hasSize(1));
        final Object event = events.get(0);
        assertThat(event, is(instanceOf(StateChangedEvent.class)));
        final StateChangedEvent<State, Event> sce = (StateChangedEvent<State, Event>) event;
        assertThat(sce.getPreviousState(), is(State.INITIAL));
        assertThat(sce.getEvent(), is(Event.A));
        assertThat(sce.getNewState(), is(State.SECOND));

        assertThat(eventCatcher.getDeadEvents(), is(empty()));
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
