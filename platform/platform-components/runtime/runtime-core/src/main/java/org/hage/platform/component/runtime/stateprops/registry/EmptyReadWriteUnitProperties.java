package org.hage.platform.component.runtime.stateprops.registry;

import lombok.RequiredArgsConstructor;
import org.hage.platform.simulation.runtime.state.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.PropertyValue;
import org.hage.platform.simulation.runtime.state.ReadWriteUnitProperties;

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
class EmptyReadWriteUnitProperties implements ReadWriteUnitProperties {

    private static final ReadWriteUnitProperties INSTANCE = new EmptyReadWriteUnitProperties();

    public static ReadWriteUnitProperties emptyProperties() {
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
    public <T extends Serializable> Optional<T> getProperty(PropertyDescriptor<T> descriptor) {
        return empty();
    }

    @Override
    public <T extends Serializable> void setProperty(PropertyDescriptor<T> descriptor, T propertyValue) {
        /* no-op */
    }

    @Override
    public <T extends Serializable> boolean checkProperty(PropertyDescriptor<T> descriptor, Predicate<T> checkPredicate) {
        return false;
    }

    @Override
    public <T extends Serializable> Optional<T> updateAndGetProperty(PropertyDescriptor<T> descriptor, UnaryOperator<T> updateFunction) {
        return empty();
    }

    @Override
    public <T extends Serializable> boolean tryUpdateProperty(PropertyDescriptor<T> descriptor, Predicate<T> updateIsLegalPredicate, UnaryOperator<T> updateFunction) {
        return false;
    }

}
