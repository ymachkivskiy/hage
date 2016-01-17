package org.hage.lifecycle;


import com.google.common.base.Objects;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;


@Immutable
public final class ExitRequestedEvent implements Event {

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }
}
