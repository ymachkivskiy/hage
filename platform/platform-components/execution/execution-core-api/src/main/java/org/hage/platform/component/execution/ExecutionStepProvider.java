package org.hage.platform.component.execution;

public interface ExecutionStepProvider {
    /**
     * @return  number of currently performed step
     */
    long getCurrentStep();

    /**
     * @return number of finished steps
     */
    long getFinishedSteps();
}
