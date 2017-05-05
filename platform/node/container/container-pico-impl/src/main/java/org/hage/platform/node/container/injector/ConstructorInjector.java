package org.hage.platform.node.container.injector;


import org.hage.platform.node.container.definition.ComponentDefinition;
import org.hage.platform.node.container.definition.IArgumentDefinition;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.Parameter.Resolver;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static java.util.Arrays.asList;
import static org.hage.util.reflect.Classes.getDeclaredConstructors;
import static org.hage.util.reflect.predicates.ConstructorPredicates.matchingActualParameters;
import static org.hage.util.reflect.predicates.ConstructorPredicates.withAnnotation;


public final class ConstructorInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ConstructorInjector.class);

    private final List<Parameter> parameters;

    private String postponedError;

     public ConstructorInjector(final ComponentDefinition definition) {
        super(definition);
        parameters = initializeParameters(definition);
    }

    private List<Parameter> initializeParameters(final ComponentDefinition definition) {
        final List<IArgumentDefinition> explicitArguments = definition.getConstructorArguments();
        if(!explicitArguments.isEmpty()) {
            return Parameters.fromList(explicitArguments);
        } else {
            @SuppressWarnings("unchecked")
            final Class<T> componentType = (Class<T>) getComponentImplementation();
            final List<Constructor<T>> constructors = getDeclaredConstructors(componentType);
            final List<Constructor<T>> annotated = newArrayList(filter(constructors, withAnnotation(Inject.class)));

            if(!annotated.isEmpty()) {
                if(annotated.size() > 1) {
                    postponedError = "At most one constructor can be annotated with @Inject";
                } else {
                    return asList(Parameters.from(getOnlyElement(annotated).getParameterTypes()));
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    protected void doVerify(final PicoContainer container) throws PicoCompositionException {
        log.debug("Verifing ConstructorInjector for component with key {} and type {}", getComponentKey(),
                  getComponentImplementation());

        // First check if there are no postponed errors from the constructor
        checkForPostponedErrors();

        // Then check if all parameters are resolvable
        for(final Parameter parameter : parameters) {
            parameter.verify(container, this, Object.class, null, false, null);
        }

        // Finally check if there is a matching constructor
        final List<Class<?>> parameterTypes = getParameterTypes(parameters, container);
        findOnlyMatchingConstructor(getComponentImplementation(), parameterTypes);
    }

    @Override
    protected T doCreate(final PicoContainer container) throws PicoCompositionException {
        log.debug("ConstructorInjector is creating component with key {} and type {}", getComponentKey(),
                  getComponentImplementation());

        checkForPostponedErrors();

        final List<Object> argumentValues = resolveAllInstances(parameters, container);
        final List<Class<?>> argumentTypes = getArgumentTypes(argumentValues);
        final Constructor<? extends T> constructor = findOnlyMatchingConstructor(getComponentImplementation(), argumentTypes);

        log.trace("Injecting arguments {} of types {} in constructor {}", argumentValues, argumentTypes,
                  constructor);
        return doCreateInstance(constructor, argumentValues.toArray());
    }

    private List<Object> resolveAllInstances(final List<Parameter> parameters, final PicoContainer container) {
        final List<Object> resolvers = newArrayListWithCapacity(parameters.size());
        for(final Parameter parameter : parameters) {
            resolvers.add(resolve(parameter, container).resolveInstance());
        }
        return resolvers;
    }

    private List<Class<?>> getArgumentTypes(final List<Object> argumentsValues) {
        final List<Class<?>> argumentTypes = newArrayListWithCapacity(argumentsValues.size());
        for(final Object value : argumentsValues) {
            argumentTypes.add(value.getClass());
        }
        return argumentTypes;
    }

    private <TT> TT doCreateInstance(final Constructor<TT> constructor, final Object[] arguments)
            throws PicoCompositionException {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(arguments);
        } catch(final IllegalArgumentException e) {
            throw new AssertionError(e); // should not happen
        } catch(final InstantiationException e) {
            // TODO Disallow abstract classes for components in definitions
            throw new PicoCompositionException("The component class cannot be abstract", e);
        } catch(final IllegalAccessException e) {
            throw new PicoCompositionException("The component constructor is not accessible", e);
        } catch(final InvocationTargetException e) {
            throw new PicoCompositionException("An exception was thrown when creating component", e);
        }
    }

    private void checkForPostponedErrors() {
        if(postponedError != null) {
            throw new PicoCompositionException(postponedError);
        }
    }

    private List<Class<?>> getParameterTypes(final List<Parameter> parameters, final PicoContainer container) {
        final List<Class<?>> parameterTypes = newArrayListWithCapacity(parameters.size());
        for(final Parameter parameter : parameters) {
            final Resolver resolver = resolve(parameter, container);
            final ComponentAdapter<?> adapter = resolver.getComponentAdapter();
            if(adapter != null) {
                parameterTypes.add(adapter.getComponentImplementation());
            } else {
                parameterTypes.add(resolver.resolveInstance().getClass());
            }
        }
        return parameterTypes;
    }

    private <TT> Constructor<TT> findOnlyMatchingConstructor(final Class<TT> componentType,
            final List<Class<?>> argumentsTypes) throws PicoCompositionException {
        final List<Constructor<TT>> constructors = getDeclaredConstructors(componentType);
        final List<Constructor<TT>> matching = newArrayList(filter(constructors,
                                                                   matchingActualParameters(argumentsTypes)));

        if(matching.isEmpty()) {
            throw new PicoCompositionException("No matching constructor found for arguments types: " + argumentsTypes);
        } else if(matching.size() > 1) {
            throw new PicoCompositionException("Multiple ambiguous constructors found for arguments types: "
                                                       + argumentsTypes + " : " + matching);
        }

        return getOnlyElement(matching);
    }

    private Resolver resolve(final Parameter parameter, final PicoContainer container) {
        return parameter.resolve(container, this, null, Object.class, null, false, null);
    }

    @Override
    public String getDescriptor() {
        return "Constructor injector";
    }
}
