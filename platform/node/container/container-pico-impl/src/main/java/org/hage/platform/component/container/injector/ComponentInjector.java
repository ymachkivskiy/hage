package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.definition.ComponentDefinition;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This injector is responsible for the lifecycle of component creation and injection.
 * <p>
 * It will first create an instance using constructor injection.
 * <p>
 * Then, it will use setter injection, annotated field and metod injection, and finally some interface injection.
 *
 * @param <T> the type of components this injector can create
 * @author AGH AgE Team
 */
public final class ComponentInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private final ComponentAdapter<T> instantiator;

    private final List<Injector<T>> injectors;

    /**
     * Creates an ComponentInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public ComponentInjector(final ComponentDefinition definition, final ComponentAdapter<T> instantiator,
            final List<Injector<T>> injectors) {
        super(checkNotNull(definition));
        this.instantiator = checkNotNull(instantiator);
        this.injectors = checkNotNull(injectors);
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        instantiator.verify(container);
        for(Injector<T> injector : injectors) {
            injector.verify(container);
        }
    }

    @Override
    public T doCreate(final PicoContainer container) throws PicoCompositionException {
        final T instance = instantiator.getComponentInstance(container, ComponentAdapter.NOTHING.class);
        for(final Injector<T> injector : injectors) {
            injector.decorateComponentInstance(container, ComponentAdapter.NOTHING.class, instance);
        }
        return instance;
    }

    @Override
    protected PicoCompositionException rewrittenException(final String message, final PicoCompositionException e) {
        return e;
    }

    @Override
    public String getDescriptor() {
        return "Component Injector";
    }
}
