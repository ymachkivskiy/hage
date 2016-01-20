package org.hage.platform.util.fsm;


import lombok.Getter;
import lombok.ToString;
import org.hage.platform.annotation.FieldsAreNonnullByDefault;
import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;


@ToString
@Immutable
@FieldsAreNonnullByDefault
@ReturnValuesAreNonnullByDefault
public class StateChangedEvent<S extends Enum<S>, E extends Enum<E>> implements Event {

    @Getter
    private final S previousState;
    @Getter
    private final E event;
    @Getter
    private final S newState;

    protected StateChangedEvent(S previousState, E event, S newState) {
        super();
        this.previousState = previousState;
        this.event = event;
        this.newState = newState;
    }

}
