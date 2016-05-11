package org.hage.platform.component.runtime.stateprops;

import com.google.common.base.Supplier;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.init.UnitPropertiesConfigurator;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.state.UnitRegisteredPropertiesProvider;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Suppliers.memoize;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;


@SingletonComponent
class UnitPropertiesConfiguratorImpl implements UnitPropertiesConfigurator, UnitRegisteredPropertiesProvider {

    private final Supplier<List<PropertyDescriptor>> propertyDescriptorsSupplier = memoize(this::getPropertyDescriptorsInternal);

    @Autowired
    private MutableInstanceContainer globalInstanceContainer;

    @Override
    public void configureWith(Class<? extends UnitPropertiesStateComponent> unitPropertiesStateComponentClazz) {
        ofNullable(unitPropertiesStateComponentClazz).ifPresent(globalInstanceContainer::addPrototypeComponent);
    }

    @Override
    public List<PropertyDescriptor> getRegisteredProperties() {
        return propertyDescriptorsSupplier.get();
    }

    private List<PropertyDescriptor> getPropertyDescriptorsInternal() {
        return ofNullable(globalInstanceContainer.getInstance(UnitRegisteredPropertiesProvider.class))
            .map(UnitRegisteredPropertiesProvider::getRegisteredProperties)
            .map(Collections::unmodifiableList)
            .orElse(emptyList());
    }

}
