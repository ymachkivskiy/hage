package org.hage.platform.simulation.container;


public interface Stateful {

    /**
     * Called just after the component instance is created and before the instance is returned to the client (note: this
     * method is called only once while creating a component instance).
     */
    void init();

    /**
     * Called just before the component instance is unregistered; this is usually performed when the platform is going
     * to shutdown. The method should be used to perform a cleanup, for instance closing network connections, removing
     * temporary files.
     *
     * @return <code>true</code> when the component is finished, <code>false</code> when component is still finishing
     * (it is long running operation)
     */
    boolean finish();
}
