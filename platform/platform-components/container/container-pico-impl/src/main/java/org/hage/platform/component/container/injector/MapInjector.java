package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.definition.MapDefinition;
import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkNotNull;


public final class MapInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(MapInjector.class);

    private final MapDefinition definition;

    private final Map<Parameter, Parameter> parameters;

    /**
     * Creates an MapInjector using a given map definition.
     *
     * @param definition the map definition to use
     */
    public MapInjector(final MapDefinition definition) {
        super(definition);
        this.definition = checkNotNull(definition);
        parameters = Parameters.fromMap(definition.getItems());
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        LOG.trace("Verifying MapInjector for definition {}.", definition);
        for(Parameter parameter : parameters.keySet()) {
            parameter.verify(container, this, definition.getElementsKeyType(), null, false, null);
        }
        for(Parameter parameter : parameters.values()) {
            parameter.verify(container, this, definition.getElementsValueType(), null, false, null);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public T doCreate(final PicoContainer container) throws PicoCompositionException {
        LOG.trace("Constructing a map from definition {}.", definition);

        try {
            final Map instance = (Map) definition.getType().newInstance();
            for(final Entry<Parameter, Parameter> entry : parameters.entrySet()) {
                Object key = doResolveKey(entry, container).resolveInstance();
                Object value = doResolveValue(entry, container).resolveInstance();
                instance.put(checkNotNull(key), checkNotNull(value));
            }
            return (T) instance;
        } catch(final InstantiationException e) {
            throw wrappedException(e);
        } catch(final IllegalAccessException e) {
            throw wrappedException(e);
        }
    }

    private Resolver doResolveKey(final Entry<Parameter, Parameter> e, final PicoContainer container) {
        return doResolve(e.getKey(), container, definition.getElementsKeyType());
    }

    private Resolver doResolveValue(final Entry<Parameter, Parameter> e, final PicoContainer container) {
        return doResolve(e.getValue(), container, definition.getElementsValueType());
    }

    private Resolver doResolve(final Parameter parameter, final PicoContainer container, final Type expectedType) {
        return parameter.resolve(container, this, null, expectedType, null, false, null);
    }

    @Override
    public String getDescriptor() {
        return "Collection injector";
    }
}
