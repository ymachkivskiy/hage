package org.hage.platform.component.services.core;


import org.hage.platform.component.exception.ComponentException;


public interface CoreComponent {


    void start() throws ComponentException;


    void pause();


    void resume();


    void stop();


    void configure();

    void teardownConfiguration();
}
