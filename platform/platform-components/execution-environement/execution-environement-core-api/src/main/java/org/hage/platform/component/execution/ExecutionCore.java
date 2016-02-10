package org.hage.platform.component.execution;


public interface ExecutionCore {


    void configure();


    void start() throws ExecutionCoreException;


    void pause();


    void resume();


    void stop();

}
