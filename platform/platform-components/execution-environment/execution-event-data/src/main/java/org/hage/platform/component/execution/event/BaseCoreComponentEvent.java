package org.hage.platform.component.execution.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;


@Immutable
@ToString
@RequiredArgsConstructor
public abstract class BaseCoreComponentEvent implements Event {
    @Getter
    private final CoreEventType type;
}
