package org.hage.platform.component.lifecycle;

public interface LifecycleEngine {
    void start();

    void performCommand(LifecycleCommand command);
}
