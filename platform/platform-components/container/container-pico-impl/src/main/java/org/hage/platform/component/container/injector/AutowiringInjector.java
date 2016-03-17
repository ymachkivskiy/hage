package org.hage.platform.component.container.injector;


import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.util.reflect.predicates.MethodPredicates;
import org.picocontainer.Parameter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Iterables.removeIf;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Collections.emptyList;
import static org.hage.platform.util.reflect.Methods.getSetterName;
import static org.hage.platform.util.reflect.Methods.isSetter;


public final class AutowiringInjector<T> extends AbstractInjector<T> {

    private static final Logger log = LoggerFactory.getLogger(AutowiringInjector.class);

    private static final Class<? extends Annotation> ANNOTATION_CLASS = Inject.class;

    private static final long serialVersionUID = 1L;

    private final Map<Field, Parameter> fields;

    private final Map<Method, Parameter[]> methods;

    public AutowiringInjector(final ComponentDefinition definition) {
        super(definition);

        final Set<String> explicitPropertiesNames = definition.getPropertyArguments().keySet();
        fields = ImmutableMap.copyOf(initializeFields(explicitPropertiesNames));
        methods = ImmutableMap.copyOf(initializeMethods(explicitPropertiesNames));
    }

    private Map<Field, Parameter> initializeFields(final Set<String> names) {
        final Class<?> componentType = getComponentImplementation();
        final Map<Field, Parameter> fields = newLinkedHashMap();
        for (final Field field : Introspector.getFilteredInjectableFields(componentType, names)) {
            fields.put(field, Parameters.from(field.getType()));
        }
        return fields;
    }

    private Map<Method, Parameter[]> initializeMethods(final Set<String> names) {
        final Class<?> componentType = getComponentImplementation();
        final Map<Method, Parameter[]> methods = newLinkedHashMap();
        for (final Method method : Introspector.getFilteredInjectableMethods(componentType, names)) {
            methods.put(method, Parameters.from(method.getParameterTypes()));
        }
        return methods;
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        for (final Field field : fields.keySet()) {
            final Parameter parameter = fields.get(field);
            try {
                parameter.verify(container, this, field.getType(), null, false, null);
            } catch (final PicoCompositionException e) {
                throw new PicoCompositionException(format("field '%1$s': %2$s", field.getName(), e.getMessage()), e);
            }
        }

        for (final Method method : methods.keySet()) {
            final Parameter[] parameters = methods.get(method);
            final Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < parameters.length; i++) {
                try {
                    parameters[i].verify(container, this, types[i], null, false, null);
                } catch (final PicoCompositionException e) {
                    throw new PicoCompositionException(format("argument %1$s of method '%2$s': %3$s", i,
                            method.getName(), e.getMessage()), e);
                }
            }
        }
    }

    @Override
    public void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
        log.debug("Will autowire instance {}", instance);

        for (final Field field : fields.keySet()) {
            final Object value = doResolve(fields.get(field), container, field.getType());
            injectField(instance, field, value);
        }

        for (final Method method : methods.keySet()) {
            final Object[] values = doResolveAll(methods.get(method), container, method.getParameterTypes());
            injectMethod(instance, method, values);
        }
    }

    private Object doResolve(final Parameter parameter, final PicoContainer container, final Type expectedType) {
        return parameter.resolve(container, this, null, expectedType, null, false, null).resolveInstance();
    }

    private void injectField(final T instance, final Field field, final Object argument) {
        log.trace("Will inject argument '{}' into field '{}'", argument, field);
        try {
            field.setAccessible(true);
            field.set(instance, argument);
        } catch (final IllegalArgumentException e) {
            throw new AssertionError(); // should not happen
        } catch (final IllegalAccessException e) {
            throw new PicoCompositionException(format("Unable to autowire field '%1$s' of %2$s: access denied.", field,
                    instance), e);
        }
    }

    private Object[] doResolveAll(final Parameter[] parameters, final PicoContainer container,
                                  final Type[] expectedTypes) {
        final Object[] values = new Object[parameters.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = doResolve(parameters[i], container, expectedTypes[i]);
        }
        return values;
    }

    private void injectMethod(final T instance, final Method method, final Object[] arguments) {
        log.trace("Will inject arguments '{}' into method '{}'", arguments, method);
        try {
            method.setAccessible(true);
            method.invoke(instance, arguments);
        } catch (final IllegalArgumentException e) {
            throw new AssertionError(); // should not happen
        } catch (final IllegalAccessException e) {
            throw new PicoCompositionException(format("Unable to autowire method '%1$s' of %2$s: access denied.",
                    method, instance), e);
        } catch (final InvocationTargetException e) {
            throw new PicoCompositionException(format(
                    "Unable to autowire method '%1$s' of %2$s: an exception happened.", method, instance), e);
        }
    }

    @Override
    public String getDescriptor() {
        return "Autowiring injector";
    }

    private static class Introspector {

        /**
         * Return all injectable fields for the given class, without those which names are in the given set.
         */
        public static Iterable<Field> getFilteredInjectableFields(final Class<?> clazz, final Set<String> filteringNames) {
            final List<Field> injectableFields = getInjectableFields(clazz);
            return Iterables.filter(injectableFields, new Predicate<Field>() {

                @Override
                public boolean apply(final Field field) {
                    return !filteringNames.contains(field.getName());
                }
            });
        }

        /**
         * Return all injectable methods for the given class, without setters which names are in the given set.
         */
        public static Iterable<Method> getFilteredInjectableMethods(final Class<?> clazz,
                                                                    final Set<String> filteringNames) {
            final List<Method> injectableMethods = getInjectableMethods(clazz);
            return Iterables.filter(injectableMethods, new Predicate<Method>() {

                @Override
                public boolean apply(final Method method) {
                    return !(isSetter(method) && filteringNames.contains(getSetterName(method)));
                }
            });
        }

        /**
         * Recursively computes a list of injectable fields. Fields from superclasses will be ordered before those from
         * subclasses.
         */
        public static List<Field> getInjectableFields(final Class<?> clazz) {
            // recursive stop condition
            if (clazz == null) {
                return emptyList();
            }

            // recursively get injectable fields from superclass
            final List<Field> injectableFields = newArrayList(getInjectableFields(clazz.getSuperclass()));
            for (final Field field : clazz.getDeclaredFields()) {
                if (isInjectable(field)) {
                    injectableFields.add(field);
                }
            }

            return injectableFields;
        }

        /**
         * Recursively computes a list of injectable methods. Methods from superclasses will be ordered before those
         * from subclasses.
         * <p>
         * In accordance with JSR-330, any overridden superclass method will not be present in the list. The overriding
         * subclass method will, only if it is itself injectable (injectability is not inherited in case of overriden
         * methods).
         */
        public static List<Method> getInjectableMethods(final Class<?> clazz) {
            // recursive stop condition
            if (clazz == null) {
                return emptyList();
            }

            // recursively get injectable methods from superclass
            final List<Method> allInjectableMethods = Lists.newLinkedList(getInjectableMethods(clazz.getSuperclass()));
            final List<Method> injectableMethods = newArrayList();

            // any overridden method will be present in the final list only if it is injectable in clazz
            for (final Method method : clazz.getDeclaredMethods()) {
                removeIf(allInjectableMethods, MethodPredicates.overriddenBy(method));
                if (isInjectable(method)) {
                    injectableMethods.add(method);
                }
            }
            allInjectableMethods.addAll(injectableMethods);

            return allInjectableMethods;
        }

        /**
         * According to JSR-330, injectable fields:
         * <ul>
         * <li>are annotated with @Inject.</li>
         * <li>are not final.</li>
         * <li>may have any otherwise valid name.</li>
         * </ul>
         */
        public static boolean isInjectable(final Field field) {
            return field.isAnnotationPresent(ANNOTATION_CLASS) && !isFinal(field.getModifiers());
        }

        /**
         * According to JSR-330, injectable methods:
         * <ul>
         * <li>are annotated with @Inject.</li>
         * <li>are not abstract.</li>
         * <li>do not declare type parameters of their own.</li>
         * <li>may return a result</li>
         * <li>may have any otherwise valid name.</li>
         * <li>accept zero or more dependencies as arguments.</li>
         * </ul>
         */
        public static boolean isInjectable(final Method method) {
            return method.isAnnotationPresent(ANNOTATION_CLASS) && !isAbstract(method.getModifiers())
                    && method.getTypeParameters().length == 0;
        }
    }
}
