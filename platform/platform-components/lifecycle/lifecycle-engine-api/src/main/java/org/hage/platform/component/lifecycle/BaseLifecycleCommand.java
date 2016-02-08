package org.hage.platform.component.lifecycle;

public enum BaseLifecycleCommand implements LifecycleCommand {

    START {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.START_COMMAND);
        }
    },

    PAUSE {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.PAUSE);
        }
    },

    STOP {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {
            lifecycleStateMachine.fire(LifecycleEvent.STOP_COMMAND);
        }
    },

    FAIL {
        @Override
        public void accept(LifecycleStateMachine lifecycleStateMachine) {

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
