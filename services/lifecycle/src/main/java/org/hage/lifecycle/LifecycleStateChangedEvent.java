package org.hage.lifecycle;


import org.hage.platform.annotation.FieldsAreNonnullByDefault;
import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.platform.util.fsm.StateChangedEvent;
import org.hage.services.core.LifecycleManager;

import javax.annotation.concurrent.Immutable;


@Immutable
@FieldsAreNonnullByDefault
@ReturnValuesAreNonnullByDefault
public class LifecycleStateChangedEvent extends StateChangedEvent<LifecycleManager.State, LifecycleManager.Event> {


    public LifecycleStateChangedEvent(LifecycleManager.State previousState,
                                      LifecycleManager.Event event, LifecycleManager.State newState) {
        super(previousState, event, newState);
    }
}
