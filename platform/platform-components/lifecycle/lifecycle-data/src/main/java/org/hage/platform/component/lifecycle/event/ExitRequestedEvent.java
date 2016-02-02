package org.hage.platform.component.lifecycle.event;


import lombok.ToString;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@ToString
@Immutable
public final class ExitRequestedEvent implements Event {
}
