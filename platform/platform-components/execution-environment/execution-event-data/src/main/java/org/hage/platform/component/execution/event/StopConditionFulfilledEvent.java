package org.hage.platform.component.execution.event;


import lombok.ToString;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@ToString
public final class StopConditionFulfilledEvent implements Event {
}
