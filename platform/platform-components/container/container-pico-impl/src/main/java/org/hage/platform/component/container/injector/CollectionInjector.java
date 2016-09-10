package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.definition.CollectionDefinition;
import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Injector implementation for collections.
 *
 * @param <T> the type of components this injector can create
 * @author AGH AgE Team
 */
public final class CollectionInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(CollectionInjector.class);

    private final CollectionDefinition definition;

    private final List<Parameter> parameters;

    /**
     * Creates an CollectionInjector using a given collection definition.
     *
     * @param definition the collection definition to use
     */
    public CollectionInjector(final CollectionDefinition definition) {
        super(definition);
        this.definition = checkNotNull(definition);
        parameters = Parameters.fromList(definition.getItems());
    }

    @Override
    public void doVerify(final PicoContainer container) throws PicoCompositionException {
        log.trace("Verifying CollectionInjector for definition {}.", definition);
        for(Parameter parameter : parameters) {
            parameter.verify(container, this, definition.getElementsType(), null, false, null);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public T doCreate(final PicoContainer container) throws PicoCompositionException {
        log.trace("Constructing a collection from definition {}.", definition);
        try {
            final Collection instance = (Collection) definition.getType().newInstance();
            for(Parameter parameter : parameters) {
                Object value = doResolve(parameter, container).resolveInstance();
                instance.add(checkNotNull(value));
            }
            return (T) instance;
        } catch(final InstantiationException e) {
            throw wrappedException(e);
        } catch(final IllegalAccessException e) {
            throw wrappedException(e);
        }
    }

    private Resolver doResolve(final Parameter parameter, final PicoContainer container) {
        return parameter.resolve(container, this, null, definition.getElementsType(), null, false, null);
    }

    @Override
    public String getDescriptor() {
        return "Collection injector";
    }
}
