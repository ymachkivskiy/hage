package org.hage.platform.component.synchronization;

public interface SynchronizationBarrier {
    void synchronizeOnStep(SynchPoint point);
}
