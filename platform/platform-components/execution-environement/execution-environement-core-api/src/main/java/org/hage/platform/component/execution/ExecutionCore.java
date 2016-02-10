package org.hage.platform.component.execution;


public interface ExecutionCore extends ComputationConfigurable {


    void start() throws ExecutionCoreException;


    void pause();


    void resume();


    void stop();

}
