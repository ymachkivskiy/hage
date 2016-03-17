package org.hage.platform.component.container;

import org.hage.platform.component.container.exception.ComponentException;


public interface Stateful {

    /**
     * Called just after the component instance is created and before the instance is returned to the client (note: this
     * method is called only once while creating a component instance).
     *
     * @throws ComponentException when any error during initialization occurs
     */
    void init() throws ComponentException;

    /**
     * Called just before the component instance is unregistered; this is usually performed when the platform is going
     * to shutdown. The method should be used to perform a cleanup, for instance closing network connections, removing
     * temporary files.
     *
     * @return <code>true</code> when the component is finished, <code>false</code> when component is still finishing
     * (it is long running operation)
     * @throws ComponentException when any error during finishing occurs
     */
    boolean finish() throws ComponentException;
}
