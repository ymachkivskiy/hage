package org.hage.platform.component.lifecycle.distr;


import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.component.lifecycle.BaseLifecycleCommand;

import javax.annotation.concurrent.ThreadSafe;


@ReturnValuesAreNonnullByDefault
@ThreadSafe
public final class LifecycleMessages {

    private LifecycleMessages() {
        // Empty
    }

    public static LifecycleMessage createStart() {
        return LifecycleMessage.create(BaseLifecycleCommand.START);
    }

    public static LifecycleMessage createExit() {
        return LifecycleMessage.create(BaseLifecycleCommand.EXIT);
    }
}
