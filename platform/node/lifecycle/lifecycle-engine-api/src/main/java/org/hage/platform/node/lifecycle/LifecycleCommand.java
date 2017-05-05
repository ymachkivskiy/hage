package org.hage.platform.node.lifecycle;

import java.io.Serializable;

public interface LifecycleCommand extends Serializable {
    void accept(LifecycleStateMachine lifecycleStateMachine);
}
