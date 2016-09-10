package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.*;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This class provides an injector that is able to inject an instance provider into ComponentInstanceProvider-aware
 * components.
 *
 * @param <T> the type of components this injector can inject into
 * @author AGH AgE Team
 */
public final class ComponentInstanceProviderAwareInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private final ComponentDefinition definition;

    /**
     * Creates an ComponentInstanceProviderAwareInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public ComponentInstanceProviderAwareInjector(final ComponentDefinition definition) {
        super(definition);
        this.definition = checkNotNull(definition);
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        if(!(container instanceof PicoMutableInstanceContainer)) {
            throw new PicoCompositionException("The container needs to implement IPicoComponentInstanceProvider");
        }
    }

    @Override
    public void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
        final PicoMutableInstanceContainer provider = (PicoMutableInstanceContainer) container;

        if(instance instanceof InstanceContainerAware) {
            ((InstanceContainerAware) instance).setInstanceProvider(provider);
        }
        if(instance instanceof MutableInstanceContainerAware) {
            ((MutableInstanceContainerAware) instance).setMutableInstanceContainer(provider);
        }
        if(instance instanceof SelfAwareInstanceContainerAware) {
            ((SelfAwareInstanceContainerAware) instance)
                    .setSelfAwareInstanceContainer(new DefaultSelfAwareInstanceContainer(provider, definition));
        }
    }

    @Override
    public String getDescriptor() {
        return "ComponentInstanceProviderAware Injector";
    }
}
