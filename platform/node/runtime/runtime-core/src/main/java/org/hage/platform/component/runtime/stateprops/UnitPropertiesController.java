package org.hage.platform.component.runtime.stateprops;

import com.google.common.base.Supplier;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.execution.monitor.SimulationExecutionMonitor;
import org.hage.platform.component.runtime.stateprops.registry.UnitPropertiesRegistry;
import org.hage.platform.component.runtime.unit.StateChangePerformer;
import org.hage.platform.component.runtime.unit.UnitComponent;
import org.hage.platform.component.runtime.unit.UnitContainer;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.state.UnitPropertiesUpdater;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.google.common.base.Suppliers.memoize;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.hage.platform.component.runtime.stateprops.PropertiesControllerInitialState.initialStateWith;

@PrototypeComponent
@Slf4j
public class UnitPropertiesController implements StateChangePerformer, UnitPropertiesProvider, UnitComponent {

    private final Position position;

    private Optional<PropertiesControllerInitialState> initialState;

    private Supplier<Optional<UnitPropertiesUpdater>> cachedUpdater;
    private Supplier<ReadWriteUnitProperties> cachedUnitProperties;

    @Setter
    private UnitContainer unitContainer;

    @Autowired
    private SimulationExecutionMonitor simulationExecutionMonitor;

    @Autowired
    public void setPropertiesRegistry(UnitPropertiesRegistry propertiesRegistry) {
        this.cachedUnitProperties = memoize(() -> propertiesRegistry.readWriteUnitPropertiesFor(position));
    }

    public UnitPropertiesController(Position position) {
        this(position, null);
    }

    public UnitPropertiesController(Position position, PropertiesControllerInitialState initialState) {
        this.position = position;
        this.initialState = ofNullable(initialState);
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

    @Override
    public void performPostConstruction() {
        applyInitialState(initialState.orElse(createInitialState()));
        initialState = empty();
    }

    private void applyInitialState(PropertiesControllerInitialState initialState) {
        this.cachedUpdater = memoize(initialState::getPropertiesUpdater);
    }

    private PropertiesControllerInitialState createInitialState() {
        return initialStateWith(unitContainer.newUnitPropertiesUpdaterInstance().orElse(null));
    }

    public Optional<UnitPropertiesUpdater> serializeUnitPropertiesUpdater() {
        log.debug("Serializing unit properties updater");

        Optional<UnitPropertiesUpdater> updater = cachedUpdater.get();
        updater.ifPresent(unitContainer::eraseDependencies);
        return updater;
    }

    private void updateStateUsing(UnitPropertiesUpdater controller) {
        log.debug("Update properties for position {}", position);
        controller.updateProperties(getUnitProperties(), position, simulationExecutionMonitor.getCurrentStepNumber());
    }

}
