package org.hage.platform.component.lifecycle;

import static org.hage.platform.component.lifecycle.LifecycleEvent.*;

public enum BaseLifecycleCommand implements LifecycleCommand {

    ASYNC__START {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(START_SIMULATION);
        }
    },

    ASYNC__PAUSE_FOR_RE_BALANCE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(PAUSE_FOR_RE_BALANCE);
        }
    },

    ASYNC__RESUME_AFTER_RE_BALANCE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(RESUME_SIMULATION);
        }
    },

    SYNC__PAUSE_FOR_RE_BALANCE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fireAndWaitForTransitionToComplete(PAUSE_FOR_RE_BALANCE);
        }
    },

    SYNC__RESUME_AFTER_RE_BALANCE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fireAndWaitForTransitionToComplete(RESUME_SIMULATION);
        }
    },

    ASYNC__STOP {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(STOP_SIMULATION);
        }
    },

    ASYNC_FAIL {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(ERROR);
        }
    },

    ASYNC_EXIT {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(EXIT);
        }
    },
}
