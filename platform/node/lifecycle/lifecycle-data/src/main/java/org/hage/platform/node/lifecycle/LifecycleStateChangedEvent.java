package org.hage.platform.node.lifecycle;


import lombok.Data;
import org.hage.platform.node.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public class LifecycleStateChangedEvent implements Event {
    private final LifecycleState previousState;
    private final LifecycleEvent event;
    private final LifecycleState newState;
}
