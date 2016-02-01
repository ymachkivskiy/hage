package org.hage.platform.util.fsm;


import lombok.Data;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public class StateChangedEvent<S extends Enum<S>, E extends Enum<E>> implements Event {
    private final S previousState;
    private final E event;
    private final S newState;
}
