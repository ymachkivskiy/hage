package org.hage.platform.component.execution.phase;

import java.util.Collection;

public interface ExecutionPhase {

    ExecutionPhaseType getType();

    Collection<? extends Runnable> getTasks(long currentStep);
}
