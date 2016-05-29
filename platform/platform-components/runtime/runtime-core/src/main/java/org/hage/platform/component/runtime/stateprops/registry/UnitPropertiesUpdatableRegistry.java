package org.hage.platform.component.runtime.stateprops.registry;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.step.StepFinalizer;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.simulation.runtime.state.UnitRegisteredPropertiesProvider;
import org.hage.platform.simulation.runtime.state.property.ReadUnitProperties;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hage.platform.component.runtime.stateprops.registry.EmptyReadWriteUnitProperties.emptyProperties;

@SingletonComponent
@Slf4j
class UnitPropertiesUpdatableRegistry implements UnitPropertiesRegistry, StepFinalizer, UnitPropertiesUpdater {

    private final Map<Position, UnitProperties> propertiesMap = new ConcurrentHashMap<>();
    private final Map<Position, ReadUnitProperties> readOnlyPropertiesCopies = new ConcurrentHashMap<>();

    @Autowired
    private Structure structure;
    @Autowired
    private UnitRegisteredPropertiesProvider unitRegisteredPropertiesProvider;

    @Override
    public ReadWriteUnitProperties readWriteUnitPropertiesFor(Position position) {
        log.debug("Query read/write unit properties for position {}", position);
        return getOrCreateUnitProperties(position);
    }

    @Override
    public ReadUnitProperties readUnitPropertiesFor(Position position) {
        log.debug("Query read unit properties for position {}", position);
        return readOnlyPropertiesCopies.computeIfAbsent(position, p -> getUnitProperties(p).createCopy());
    }

    @Override
    public void finalizeStep() {
        log.debug("Clear all property copies");
        readOnlyPropertiesCopies.clear();
    }

    @Override
    public List<PositionUnitProperties> getUnitProperties() {
        log.debug("Get all unit properties");

        return propertiesMap.entrySet().stream()
            .map(e -> new PositionUnitProperties(e.getKey(), e.getValue()))
            .collect(toList());
    }

    @Override
    public void updatePropertiesUsing(Collection<PositionUnitProperties> newProperties) {
        log.debug("Update unit properties with {}", newProperties);

        Map<Position, UnitProperties> updatedProperties = newProperties.stream()
            .collect(
                toMap(
                    PositionUnitProperties::getPosition,
                    PositionUnitProperties::getProperties
                )
            );

        propertiesMap.putAll(updatedProperties);
    }

    private UnitProperties getOrCreateUnitProperties(Position position) {
        return withCheckedPosition(position, pos -> propertiesMap.computeIfAbsent(pos, this::createNewProperties));
    }

    private UnitProperties getUnitProperties(Position position) {
        return withCheckedPosition(position, pos -> propertiesMap.getOrDefault(pos, emptyProperties()));
    }

    private UnitProperties withCheckedPosition(Position position, Function<Position, UnitProperties> creationFunction) {
        if (position != null && structure.belongsToStructure(position)) {
            return creationFunction.apply(position);
        } else {
            return emptyProperties();
        }
    }

    private UnitProperties createNewProperties(Position position) {
        log.debug("Create new properties for position {}", position);
        return new BaseUnitProperties(unitRegisteredPropertiesProvider.getRegisteredProperties());
    }

}
