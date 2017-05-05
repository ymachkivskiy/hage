package org.hage.platform.node.runtime.stateprops.registry;

import lombok.RequiredArgsConstructor;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.property.PropertyValue;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
class EmptyReadWriteUnitProperties implements UnitProperties {

    private static final UnitProperties INSTANCE = new EmptyReadWriteUnitProperties();

    public static UnitProperties emptyProperties() {
        return INSTANCE;
    }

    @Override
    public List<PropertyDescriptor> getDescriptors() {
        return emptyList();
    }

    @Override
    public Collection<PropertyValue> getValues() {
        return emptyList();
    }

    @Override
    public <T extends Serializable> Optional<T> get(PropertyDescriptor<T> descriptor) {
        return empty();
    }

    @Override
    public <T extends Serializable> void set(PropertyDescriptor<T> descriptor, T value) {
        /* no-op */
    }

    @Override
    public <T extends Serializable> boolean check(PropertyDescriptor<T> descriptor, Predicate<T> checkPredicate) {
        return false;
    }

    @Override
    public <T extends Serializable> Optional<T> updateAndGet(PropertyDescriptor<T> descriptor, UnaryOperator<T> updateFunction) {
        return empty();
    }

    @Override
    public <T extends Serializable> boolean tryUpdate(PropertyDescriptor<T> descriptor, Predicate<T> updateIsLegalPredicate, UnaryOperator<T> updateFunction) {
        return false;
    }

    @Override
    public UnitProperties createCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "EmptyUnitProperties()";
    }
}
