package org.hage.platform.component.lifecycle;


import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class LifecycleEngineEngineServiceTest {
//
//    // XXX: Explicit types because of javac bug
//    // http://stackoverflow.com/questions/2858799/generics-compiles-and-runs-in-eclipse-but-doesnt-compile-in-javac
//    // May be removed after dropping Java 6
//    private final LifecycleEngineServiceBuilder<State, Event> builder = LifecycleEngineServiceBuilder.<State, Event> create();
//    private LifecycleEngineService<State, Event> service;
//
//    @Before
//    public void setUp() {
//        builder.states(State.class).events(Event.class);
//        builder.startWith(State.INITIAL);
//        builder.terminateIn(State.TERMINAL);
//        builder.inAnyState().on(Event.ERROR).goTo(State.FAIL).commit();
//        builder.ifFailed().fire(Event.ERROR);
//        builder.withEventBus(mock(EventBus.class));
//        builder.notificationCreator(mock(NotificationEventCreator.class));
//    }
//
//    @Test(timeout = 10000)
//    public void shouldBeInCorrectInitialState() {
//        // given
//        service = builder.build();
//
//        // then
//        assertThat(service.getCurrentState(), is(State.INITIAL));
//    }
//
//    @Test(timeout = 10000)
//    public void shouldPerformBasicTransition() {
//        // given
//        builder.in(State.INITIAL).on(Event.A).goTo(State.SECOND).commit();
//        service = builder.build();
//
//        // when
//        service.fireAndWaitForTransitionToComplete(Event.A);
//
//        // then
//        assertThat(service.getCurrentState(), is(State.SECOND));
//    }
//
//    @Test(timeout = 10000)
//    public void shouldBeTerminatedAfterTransitionToTerminalState() {
//        // given
//        builder.in(State.INITIAL).on(Event.A).goTo(State.TERMINAL).commit();
//        service = builder.build();
//
//        // when
//        service.fireAndWaitForTransitionToComplete(Event.A);
//
//        // then
//        assertThat(service.terminated(), is(true));
//        assertThat(service.getCurrentState(), is(State.TERMINAL));
//    }
//
//    @Test(timeout = 10000)
//    public void shouldExecuteActionOnTransition() {
//        // given
//        final AtomicBoolean executed = new AtomicBoolean(false);
//        builder.in(State.INITIAL).on(Event.A).execute(new Runnable() {
//
//            @Override
//            public void run() {
//                executed.set(true);
//            }
//        }).goTo(State.SECOND).commit();
//        service = builder.build();
//
//        // when
//        service.fireAndWaitForTransitionToComplete(Event.A);
//
//        // then
//        assertThat(service.getCurrentState(), is(State.SECOND));
//        assertThat(executed.get(), is(true));
//    }
//
//    /**
//     * Undefined transition should result in failure event and fail state.
//     */
//    @Test(timeout = 10000)
//    public void shouldFailIfTransitionUndefined() throws InterruptedException {
//        // given
//        service = builder.build();
//
//        // when
//        service.fireAndWaitForStableState(Event.A);
//
//        // then
//        assertThat(service.getCurrentState(), is(State.FAIL));
//    }
//
//    @Test(timeout = 10000)
//    public void shouldIgnoreEventsWhenTerminated() throws InterruptedException {
//        // given
//        builder.in(State.INITIAL).on(Event.A).goTo(State.TERMINAL).commit();
//        builder.in(State.TERMINAL).on(Event.B).goTo(State.SECOND).commit();
//        service = builder.build();
//
//        // when
//        service.fireAndWaitForTransitionToComplete(Event.A);
//        service.fireAndWaitForTransitionToComplete(Event.B);
//
//        // then
//        assertThat(service.getCurrentState(), is(State.TERMINAL));
//    }
//
//    @Ignore
//    @SuppressWarnings("unchecked")
//    @Test(timeout = 10000)
//    public void shouldSendNotificationsAboutStateChanges() {
//        // given
//        final EventCatcher eventCatcher = new EventCatcher();
//        builder.in(State.INITIAL).on(Event.A).goTo(State.SECOND).commit();
//        service = builder.build();
////        service.getEventBus().register(eventCatcher);
//
//        // when
//        service.fireAndWaitForTransitionToComplete(Event.A);
//
//        // then
//        final List<Object> events = eventCatcher.getCaughtEvents();
//        assertThat(events, hasSize(1));
//        final Object event = events.get(0);
//        assertThat(event, is(instanceOf(LifecycleStateChangedEvent.class)));
//        final LifecycleStateChangedEvent<State, Event> sce = (LifecycleStateChangedEvent<State, Event>) event;
//        assertThat(sce.getPreviousState(), is(State.INITIAL));
//        assertThat(sce.getEvent(), is(Event.A));
//        assertThat(sce.getNewState(), is(State.SECOND));
//
//        assertThat(eventCatcher.getDeadEvents(), is(empty()));
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
