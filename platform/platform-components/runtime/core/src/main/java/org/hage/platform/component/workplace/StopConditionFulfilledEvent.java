package org.hage.platform.component.workplace;


import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.toStringHelper;

@Immutable
public class StopConditionFulfilledEvent implements Event {

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
