package org.hage.platform.component.execution;


public interface ExecutionCore {

    /**
     * Starts or resumes execution.
     *
     * Has effect only in <b>stopped</b> or <b>paused</b> core states.
     *
     * @throws ExecutionCoreException
     */
    void start();

    /**
     * Pause execution after performing full execution cycle. <b>Will block</b> until execution cycle is not finished.
     *
     * Has effect only in <b>running</b> core state.
     */
    void pause();

    /**
     * Stops current execution. <b>Will block</b> until execution cycle is not finished.
     *
     * After stopping execution all execution tasks <b>will be removed</b> from core.
     */
    void stop();

    /**
     * @return  execution info for finished execution cycles.
     */
    ExecutionInfo getInfo();

}
