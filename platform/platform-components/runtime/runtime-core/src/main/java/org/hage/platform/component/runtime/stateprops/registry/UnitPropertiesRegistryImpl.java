package org.hage.platform.component.runtime.stateprops.registry;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.simulation.runtime.state.ReadWriteUnitProperties;
import org.hage.platform.simulation.runtime.state.UnitRegisteredPropertiesProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.hage.platform.component.runtime.stateprops.registry.EmptyReadWriteUnitProperties.emptyProperties;

@SingletonComponent
@Slf4j
class UnitPropertiesRegistryImpl implements UnitPropertiesRegistry {

    private final Map<Position, ReadWriteUnitProperties> propertiesMap = new ConcurrentHashMap<>();

    @Autowired
    private Structure structure;
    @Autowired
    private UnitRegisteredPropertiesProvider unitRegisteredPropertiesProvider;

    @Override
    public ReadWriteUnitProperties unitPropertiesFor(Position position) {
        log.debug("Query unit properties for position {}", position);

        if (position != null && structure.belongsToStructure(position)) {
            return propertiesMap.computeIfAbsent(position, this::createNewProperties);
        } else {
            log.warn("Query for unit properties for position which does not belong to computation structure {}", position);
            return emptyProperties();
        }
    }

    private ReadWriteUnitProperties createNewProperties(Position position) {
        log.debug("Create new properties for position {}", position);
        return new BaseReadWriteUnitProperties(unitRegisteredPropertiesProvider.getRegisteredProperties());
    }


}
