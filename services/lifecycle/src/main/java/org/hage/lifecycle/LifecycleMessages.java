package org.hage.lifecycle;


import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;

import javax.annotation.concurrent.ThreadSafe;


@ReturnValuesAreNonnullByDefault
@ThreadSafe
public final class LifecycleMessages {

    private LifecycleMessages() {
        // Empty
    }

    public static LifecycleMessage createStart() {
        return LifecycleMessage.create(LifecycleMessage.LifecycleCommand.START);
    }

    public static LifecycleMessage createExit() {
        return LifecycleMessage.create(LifecycleMessage.LifecycleCommand.EXIT);
    }
}
