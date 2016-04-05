package org.hage.platform.component.runtime.execution;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseStaticExecutionStateAware implements ExecutionStepAware {
    private final PostStepPhase phase;

    @Override
    public PostStepPhase getPhase() {
        return this.phase;
    }

}
