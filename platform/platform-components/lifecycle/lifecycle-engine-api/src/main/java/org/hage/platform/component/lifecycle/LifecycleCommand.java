package org.hage.platform.component.lifecycle;

import java.io.Serializable;

public interface LifecycleCommand extends Serializable {
    void accept(LifecycleStateMachine lifecycleStateMachine);
}
