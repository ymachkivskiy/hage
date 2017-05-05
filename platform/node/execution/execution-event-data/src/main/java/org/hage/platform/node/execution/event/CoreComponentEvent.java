package org.hage.platform.node.execution.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.node.bus.Event;

import javax.annotation.concurrent.Immutable;


@Immutable
@ToString
@RequiredArgsConstructor
public abstract class CoreComponentEvent implements Event {
    @Getter
    private final CoreEventType type;
}
