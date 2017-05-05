package org.hage.platform.node.container.injector;


import org.hage.platform.node.container.definition.ArrayDefinition;
import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Injector implementation for arrays.
 *
 * @param <T> the type of components this injector can create
 * @author AGH AgE Team
 */
public final class ArrayInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ArrayInjector.class);

    private final ArrayDefinition definition;

    private final List<Parameter> parameters;

    /**
     * Creates an ArrayInjector using a given array definition.
     *
     * @param definition the array definition to use
     */
    public ArrayInjector(final ArrayDefinition definition) {
        super(definition);
        this.definition = checkNotNull(definition);
        parameters = Parameters.fromList(definition.getItems());
    }

    @Override
    public void doVerify(final PicoContainer container) throws PicoCompositionException {
        log.trace("Verifying ArrayInjector for definition {}.", definition);
        for(Parameter parameter : parameters) {
            parameter.verify(container, this, definition.getElementsType(), null, false, null);
        }
    }

    @Override
    public T doCreate(final PicoContainer container) throws PicoCompositionException {
        log.trace("Constructing an array from definition {}.", definition);

        final List<Object> values = new ArrayList<Object>();
        for(Parameter parameter : parameters) {
            Object value = doResolve(parameter, container).resolveInstance();
            values.add(checkNotNull(value));
        }

        @SuppressWarnings("unchecked")
        final T instance = (T) values.toArray((Object[]) Array.newInstance(definition.getElementsType(), values.size()));
        return instance;
    }

    private Resolver doResolve(final Parameter parameter, final PicoContainer container) {
        return parameter.resolve(container, this, null, definition.getElementsType(), null, false, null);
    }

    @Override
    public String getDescriptor() {
        return "Array injector";
    }
}
