package org.hage.platform.simulation.runtime.state.property;

import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface WriteUnitProperties {

    <T extends Serializable> void set(PropertyDescriptor<T> descriptor, T value);

    <T extends Serializable> Optional<T> updateAndGet(PropertyDescriptor<T> descriptor, UnaryOperator<T> updateFunction);

    <T extends Serializable> boolean tryUpdate(PropertyDescriptor<T> descriptor, Predicate<T> updateIsLegalPredicate, UnaryOperator<T> updateFunction);

}
