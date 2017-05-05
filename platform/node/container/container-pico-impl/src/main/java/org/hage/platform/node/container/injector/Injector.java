package org.hage.platform.node.container.injector;


import java.lang.reflect.Type;


/**
 * A component adapter that is responsible for instantiating components and injecting dependencies.
 *
 * @param <T> the type of components this injector can create or inject into
 * @author AGH AgE Team
 */
public interface Injector<T> extends org.picocontainer.Injector<T> {

    /**
     * Checks whether this injector can produce an instance of a parametrised class.
     *
     * @param type           A raw class that should be produced.
     * @param typeParameters An array of type parameters.
     * @return True, if {@link Injector#getParametrizedInstance} with the same parameters will return some instance.
     */
    boolean canProduceParametrizedInstance(Class<?> type, Type[] typeParameters);

    <TT> TT getParametrizedInstance(final Class<TT> type, final Type[] typeParameters);
}
