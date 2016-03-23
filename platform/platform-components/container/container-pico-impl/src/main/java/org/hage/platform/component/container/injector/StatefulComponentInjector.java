package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.exception.ComponentException;
import org.hage.platform.simulation.container.Stateful;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

public final class StatefulComponentInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an StatefulComponentInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public StatefulComponentInjector(final ComponentDefinition definition) {
        super(definition);
    }

    @Override
    public void doInject(final PicoContainer provider, final T instance) throws PicoCompositionException {
        if(instance instanceof Stateful) {
            try {
                ((Stateful) instance).init();
            } catch(final ComponentException e) {
                throw new PicoCompositionException("Exception happenned during component initialization", e);
            }
        }
    }

    @Override
    public String getDescriptor() {
        return "StatefulComponent Injector";
    }
}
