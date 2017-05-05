package org.hage.platform.cluster.synch;

public interface SynchronizationBarrier {
    void synchronize(SynchPoint point);
}
