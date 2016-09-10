package org.hage.platform.component.container.injector;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hage.platform.component.container.definition.*;
import org.picocontainer.*;
import org.picocontainer.injectors.AbstractInjector.UnsatisfiableDependenciesException;
import org.picocontainer.parameters.BasicComponentParameter;
import org.picocontainer.parameters.ConstantParameter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;


public final class Parameters {

    private Parameters() {
    }

    public static List<Parameter> fromList(final List<IArgumentDefinition> argumentDefinitions) {
        List<Parameter> parameters = Lists.newArrayListWithCapacity(argumentDefinitions.size());
        for(IArgumentDefinition argumentDefinition : argumentDefinitions) {
            parameters.add(from(argumentDefinition));
        }
        return parameters;
    }

    public static Parameter from(final IArgumentDefinition argumentDefinition) {
        if(argumentDefinition instanceof ReferenceDefinition) {
            ReferenceDefinition referenceDefinition = (ReferenceDefinition) argumentDefinition;
            return new StrictComponentParameter(referenceDefinition.getTargetName());
        } else {
            ValueDefinition valueDefinition = (ValueDefinition) argumentDefinition;
            try {
                Object value = SimpleTypeParsers.parse(valueDefinition.getStringValue(),
                                                       valueDefinition.getDesiredClass());
                return new ConstantParameter(value);
            } catch(ConfigurationException e) {
                throw new PicoCompositionException(e);
            }
        }
    }

    public static Map<Parameter, Parameter> fromMap(
            final Map<IArgumentDefinition, IArgumentDefinition> argumentDefinitions) {
        Map<Parameter, Parameter> parameters = Maps.newHashMapWithExpectedSize(argumentDefinitions.size());
        for(Entry<IArgumentDefinition, IArgumentDefinition> e : argumentDefinitions.entrySet()) {
            parameters.put(from(e.getKey()), from(e.getValue()));
        }
        return parameters;
    }

    public static <K> Map<K, Parameter> fromMapping(final Map<K, IArgumentDefinition> mapping) {
        Map<K, Parameter> parameters = Maps.newHashMapWithExpectedSize(mapping.size());
        for(Entry<K, IArgumentDefinition> e : mapping.entrySet()) {
            parameters.put(e.getKey(), from(e.getValue()));
        }
        return parameters;
    }

    public static Parameter[] from(final Class<?>[] types) {
        final Parameter[] parameters = new Parameter[types.length];
        for(int i = 0; i < parameters.length; i++) {
            parameters[i] = from(types[i]);
        }
        return parameters;
    }

    public static Parameter from(final Class<?> type) {
        return new StrictComponentParameter(type);
    }

    static final class StrictComponentParameter extends BasicComponentParameter {

        private static final long serialVersionUID = 1L;

        private final Object key;

        public StrictComponentParameter(final Object key) {
            this.key = checkNotNull(key);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        protected <T> ComponentAdapter<T> resolveAdapter(final PicoContainer container, final ComponentAdapter adapter,
                final Class<T> expectedType, final NameBinding expectedNameBinding, final boolean useNames,
                final Annotation binding) {
            // First search by value
            ComponentAdapter<T> result = (ComponentAdapter<T>) container.getComponentAdapter(key);
            if(result == null && (key instanceof Class)) {
                // Otherwise, if the key is a class, try to look up by type
                result = container.getComponentAdapter((Class<T>) key, (NameBinding) null);
            }

            if(result == null || !expectedType.isAssignableFrom(result.getComponentImplementation())) {
                throw new UnsatisfiableDependenciesException(format("unsatisfied dependency '%1$s' in %2$s ", key,
                                                                    container));
            }
            return result;
        }
    }
}
