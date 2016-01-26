package org.hage.platform.util.bus;

import com.google.common.eventbus.EventBus;

class SynchronousEventBus extends BaseEventBus {
    private static final String BUS_NAME = "HageSynchEventBus";

    public SynchronousEventBus() {
        super(new EventBus(BUS_NAME));
    }

}
