package org.hage.platform.node.bus;

import com.google.common.eventbus.EventBus;

public class SynchronousEventBus extends BaseEventBus {
    private static final String BUS_NAME = "HageSynchEventBus";

    public SynchronousEventBus() {
        super(new EventBus(BUS_NAME));
    }

}
