package org.hage.platform.component.lifecycle.communication;


import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.component.lifecycle.LifecycleCommand;

import javax.annotation.concurrent.ThreadSafe;


@ReturnValuesAreNonnullByDefault
@ThreadSafe
public final class LifecycleMessages {

    private LifecycleMessages() {
        // Empty
    }

    public static LifecycleMessage createStart() {
        return LifecycleMessage.create(LifecycleCommand.START);
    }

    public static LifecycleMessage createExit() {
        return LifecycleMessage.create(LifecycleCommand.EXIT);
    }
}
