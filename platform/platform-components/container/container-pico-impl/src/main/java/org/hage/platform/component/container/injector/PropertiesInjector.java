package org.hage.platform.component.container.injector;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;
import static org.apache.commons.beanutils.PropertyUtils.*;


/**
 * A properties injector. Will inject javabeans properties with the arguments specified in a given component definition.
 *
 * @param <T> the type of components this injector can inject into
 * @author AGH AgE Team
 */
public final class PropertiesInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesInjector.class);

    private final Map<String, Parameter> parameters;

    private final Map<String, PropertyDescriptor> descriptors;

    /**
     * Creates an PropertiesInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public PropertiesInjector(final ComponentDefinition definition) {
        super(definition);
        parameters = Parameters.fromMapping(definition.getPropertyArguments());
        descriptors = initializeDescriptors();
    }

    private Map<String, PropertyDescriptor> initializeDescriptors() {
        Builder<String, PropertyDescriptor> builder = ImmutableMap.builder();
        for(PropertyDescriptor descriptor : getPropertyDescriptors(getComponentImplementation())) {
            builder.put(descriptor.getName(), descriptor);
        }
        return builder.build();
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        for(final String propertyName : parameters.keySet()) {
            PropertyDescriptor descriptor = descriptors.get(propertyName);
            if(descriptor == null) {
                throw new PicoCompositionException(format("no property named '%1$s'", propertyName));
            }
            Class<?> propertyType = descriptor.getPropertyType();

            Parameter parameter = parameters.get(propertyName);
            try {
                parameter.verify(container, this, propertyType, null, false, null);
            } catch(PicoCompositionException e) {
                throw new PicoCompositionException(format("property '%1$s': %2$s", propertyName, e.getMessage()), e);
            }
        }
    }

    @Override
    public void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
        LOG.debug("Properties injector will inject properties into {}", instance);

        for(final Entry<String, Parameter> e : parameters.entrySet()) {
            injectProperty(instance, container, e.getKey(), e.getValue());
        }
    }

    private void injectProperty(final T instance, final PicoContainer container, final String propertyName,
            final Parameter propertyParameter) {
        try {
            Class<?> expectedType = getPropertyType(instance, propertyName);
            Object value = doResolve(propertyParameter, container, expectedType).resolveInstance();
            setProperty(instance, propertyName, value);
        } catch(final IllegalAccessException e) {
            throw wrappedException(e);
        } catch(final InvocationTargetException e) {
            throw wrappedException(e);
        } catch(final NoSuchMethodException e) {
            throw wrappedException(e);
        }
    }

    private Resolver doResolve(final Parameter parameter, final PicoContainer container, final Type expectedType) {
        return parameter.resolve(container, this, null, expectedType, null, false, null);
    }

    @Override
    public String getDescriptor() {
        return "Properties injector";
    }
}
