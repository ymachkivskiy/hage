package org.hage.platform.util.fsm;


public interface NotificationEventCreator<S extends Enum<S>, E extends Enum<E>, NotificationEvent extends StateChangedEvent<S, E>> {
    NotificationEvent create(S previousState, E event, S newState);
}
