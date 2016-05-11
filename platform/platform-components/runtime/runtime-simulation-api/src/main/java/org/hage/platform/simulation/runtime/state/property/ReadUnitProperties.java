package org.hage.platform.simulation.runtime.state.property;

import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface ReadUnitProperties extends Serializable {

    Collection<PropertyDescriptor> getDescriptors();

    Collection<PropertyValue> getValues();

    <T extends Serializable> Optional<T> getProperty(PropertyDescriptor<T> descriptor);

    <T extends Serializable> boolean checkProperty(PropertyDescriptor<T> descriptor, Predicate<T> checkPredicate);
}
