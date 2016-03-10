package org.hage.platform.component.lifecycle;

import static org.hage.platform.component.lifecycle.LifecycleEvent.ERROR;

public enum BaseLifecycleCommand implements LifecycleCommand {

    START {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.START_SIMULATION);
        }
    },

    PAUSE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.PAUSE_SIMULATION);
        }
    },

    STOP {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.STOP_SIMULATION);
        }
    },

    FAIL {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(ERROR);
        }
    },

    NOTIFY {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {

        }
    },

    EXIT {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.EXIT);
        }
    },
}
