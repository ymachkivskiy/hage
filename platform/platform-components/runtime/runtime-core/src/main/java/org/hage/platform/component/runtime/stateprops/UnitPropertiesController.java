package org.hage.platform.component.runtime.stateprops;

import com.google.common.base.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.execution.monitor.ExecutionMonitor;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.stateprops.registry.UnitPropertiesRegistry;
import org.hage.platform.component.runtime.unit.faces.StateChangePerformer;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.state.UnitPropertiesUpdater;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.google.common.base.Suppliers.memoize;
import static java.util.Optional.ofNullable;

@PrototypeComponent
@Slf4j
public class UnitPropertiesController implements StateChangePerformer, UnitPropertiesProvider {

    private final Position position;

    private final Supplier<Optional<UnitPropertiesUpdater>> cachedUpdater;
    private Supplier<ReadWriteUnitProperties> cachedUnitProperties;

    @Autowired
    private ExecutionMonitor executionMonitor;

    @Autowired
    public void setPropertiesRegistry(UnitPropertiesRegistry propertiesRegistry) {
        this.cachedUnitProperties = memoize(() -> propertiesRegistry.readWriteUnitPropertiesFor(position));
    }

    public UnitPropertiesController(Position position, UnitComponentCreationController creationController) {
        this.position = position;
        this.cachedUpdater = memoize(() -> ofNullable(creationController.getInstanceContainer().getInstance(UnitPropertiesUpdater.class)));
    }

    @Override
    public void performStateChange() {
        cachedUpdater.get().ifPresent(this::updateStateUsing);
    }

    @Override
    public ReadWriteUnitProperties getUnitProperties() {
        log.debug("Get read/write unit properties for {}", position);
        return cachedUnitProperties.get();
    }

    private void updateStateUsing(UnitPropertiesUpdater controller) {
        log.debug("Update properties for position {}", position);
        controller.updateProperties(getUnitProperties(), position, executionMonitor.getCurrentStepNumber());
    }

}
