package org.hage.platform.node.runtime.stateprops.registry;

import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.property.PropertyValue;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Collections.unmodifiableSet;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class BaseUnitProperties implements UnitProperties {

    private final Map<PropertyDescriptor, ElementWrapper> propertyValues;

    public BaseUnitProperties(List<PropertyDescriptor> descriptors) {
        propertyValues = descriptors.stream()
            .collect(toMap(
                identity(),
                e -> ElementWrapper.emptyWrapper()
            ));
    }

    private BaseUnitProperties(Map<PropertyDescriptor, ElementWrapper> propertyValues) {
        this.propertyValues = propertyValues.entrySet()
            .stream()
            .collect(
                toMap(
                    Entry::getKey,
                    e -> e.getValue().createCopyUsing(e.getKey())
                )
            );
    }

    @Override
    public Collection<PropertyDescriptor> getDescriptors() {
        return unmodifiableSet(propertyValues.keySet());
    }

    @Override
    public Collection<PropertyValue> getValues() {
        return propertyValues.entrySet()
            .stream()
            .map(e -> new PropertyValue(e.getKey(), e.getValue().getElement()))
            .collect(toList());
    }

    @Override
    public <T extends Serializable> Optional<T> get(PropertyDescriptor<T> descriptor) {
        return propertyValues.get(checkedDescriptor(descriptor)).toOptionalUsing(descriptor);
    }

    @Override
    public <T extends Serializable> boolean check(PropertyDescriptor<T> descriptor, Predicate<T> checkPredicate) {
        return get(descriptor)
            .map(checkPredicate::test)
            .orElse(false);
    }

    @Override
    public <T extends Serializable> void set(PropertyDescriptor<T> descriptor, T value) {
        propertyValues.put(checkedDescriptor(descriptor), ElementWrapper.ofSerializable(value));
    }


    @Override
    public <T extends Serializable> Optional<T> updateAndGet(PropertyDescriptor<T> descriptor, UnaryOperator<T> updateFunction) {
        return propertyValues
            .compute(
                checkedDescriptor(descriptor),
                (d, oldVal) -> oldVal.mapped(descriptor, updateFunction))
            .toOptionalUsing(descriptor);
    }

    @Override
    public <T extends Serializable> boolean tryUpdate(PropertyDescriptor<T> descriptor, Predicate<T> updateIsLegalPredicate, UnaryOperator<T> updateFunction) {
        Optional<T> oldValue = get(descriptor);

        if (oldValue.filter(updateIsLegalPredicate).isPresent()) {
            propertyValues.put(descriptor, ElementWrapper.ofSerializable(updateFunction.apply(oldValue.get())));
            return true;
        }

        return false;
    }

    @Override
    public UnitProperties createCopy() {
        return new BaseUnitProperties(propertyValues);
    }

    private <T extends Serializable> PropertyDescriptor<T> checkedDescriptor(PropertyDescriptor<T> descriptor) {
        if (!propertyValues.containsKey(descriptor)) {
            throw new IllegalArgumentException("Illegal property descriptor" + descriptor + ". Allowed property descriptors: " + getDescriptors());
        }

        return descriptor;
    }

    @Override
    public String toString() {
        return "UnitProperties(" + getValues().toString() + ")";
    }

}
