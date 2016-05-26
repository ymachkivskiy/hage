package org.hage.platform.component.lifecycle;

import static org.hage.platform.component.lifecycle.LifecycleEvent.*;

public enum BaseLifecycleCommand implements LifecycleCommand {

    A_SYNC_START {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(START_SIMULATION);
        }
    },

    A_SYNC_PAUSE_FOR_RE_BALANCE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(PAUSE_FOR_RE_BALANCE);
        }
    },

    A_SYNC_RESUME_AFTER_RE_BALANCE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(RESUME_SIMULATION);
        }
    },

    A_SYNC_STOP {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(STOP_SIMULATION);
        }
    },

    A_SYNC_FAIL {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(ERROR);
        }
    },

    A_SYNC_EXIT {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(EXIT);
        }
    },
}
