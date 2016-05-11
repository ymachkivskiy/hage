package org.hage.platform.component.runtime.stateprops.registry;

import org.hage.platform.simulation.runtime.state.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.PropertyValue;
import org.hage.platform.simulation.runtime.state.ReadWriteUnitProperties;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class BaseReadWriteUnitProperties implements ReadWriteUnitProperties {

    private final Map<PropertyDescriptor, Optional<Object>> propertyValues;

    public BaseReadWriteUnitProperties(List<PropertyDescriptor> descriptors) {
        propertyValues = descriptors.stream()
            .collect(toMap(
                identity(),
                d -> empty()
            ));
    }

    @Override
    public Collection<PropertyDescriptor> getDescriptors() {
        return unmodifiableSet(propertyValues.keySet());
    }

    @Override
    public Collection<PropertyValue> getValues() {
        return propertyValues.entrySet()
            .stream()
            .map(e -> new PropertyValue(e.getKey(), e.getValue().orElse(null)))
            .collect(toList());
    }

    @Override
    public <T extends Serializable> Optional<T> getProperty(PropertyDescriptor<T> descriptor) {
        return propertyValues.get(checkedDescriptor(descriptor)).map(descriptor.getType()::cast);
    }

    @Override
    public <T extends Serializable> boolean checkProperty(PropertyDescriptor<T> descriptor, Predicate<T> checkPredicate) {
        return getProperty(descriptor)
            .map(checkPredicate::test)
            .orElse(false);
    }

    @Override
    public <T extends Serializable> void setProperty(PropertyDescriptor<T> descriptor, T propertyValue) {
        propertyValues.put(checkedDescriptor(descriptor), of(propertyValue));
    }


    @Override
    public <T extends Serializable> Optional<T> updateAndGetProperty(PropertyDescriptor<T> descriptor, UnaryOperator<T> updateFunction) {
        return propertyValues
            .compute(
                checkedDescriptor(descriptor),
                (d, oldVal) -> oldVal.map(descriptor.getType()::cast).map(updateFunction))
            .map(descriptor.getType()::cast);
    }

    @Override
    public <T extends Serializable> boolean tryUpdateProperty(PropertyDescriptor<T> descriptor, Predicate<T> updateIsLegalPredicate, UnaryOperator<T> updateFunction) {
        Optional<T> oldValue = propertyValues.get(checkedDescriptor(descriptor)).map(descriptor.getType()::cast);

        if (oldValue.filter(updateIsLegalPredicate).isPresent()) {
            propertyValues.put(descriptor, oldValue.map(updateFunction));
            return true;
        }

        return false;
    }

    private <T extends Serializable> PropertyDescriptor<T> checkedDescriptor(PropertyDescriptor<T> descriptor) {
        if (!propertyValues.containsKey(descriptor)) {
            throw new IllegalArgumentException("Illegal property \"" + descriptor.getName() + "\" with value of type: " + descriptor.getType() + ". Allowed properties: " + getDescriptors());
        }

        return descriptor;
    }
}
