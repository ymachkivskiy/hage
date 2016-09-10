package org.hage.platform.component.lifecycle;


import lombok.Data;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public class LifecycleStateChangedEvent implements Event {
    private final LifecycleState previousState;
    private final LifecycleEvent event;
    private final LifecycleState newState;
}
