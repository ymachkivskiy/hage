package org.hage.platform.component.synchronization;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummySynchronizer implements SynchronizationBarrier {

    @Override
    public void synchronize() {
        //todo : NOT IMPLEMENTED
        log.debug("MAKE SYNCHRONIZATION");
    }
}
