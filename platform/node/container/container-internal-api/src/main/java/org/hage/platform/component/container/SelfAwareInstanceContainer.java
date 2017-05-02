package org.hage.platform.component.container;


public interface SelfAwareInstanceContainer extends InstanceContainer {

    /**
     * Returns the component instance which is supplied by provider by default.
     *
     * @return required object or null if object not found
     */
    Object getInstance();

    /**
     * Returns name of component which is supplied by provider by default.
     *
     * @return component name
     */
    String getName();

}
