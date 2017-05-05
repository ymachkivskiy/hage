package org.hage.platform.node.bus;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

// TODO: 09.03.16 switch to worker executor
public class AsynchronousEventBus extends BaseEventBus {
    private static final String BUS_NAME = "HageAsynchEventBus";
    private static final String NAME_PREFIX = "hage-asynchbus-t-";

    public AsynchronousEventBus() {
        super(new AsyncEventBus(BUS_NAME, newSingleThreadExecutor(new CustomizableThreadFactory(NAME_PREFIX))));
    }
}
