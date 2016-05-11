package org.hage.platform.simulation.runtime.context;

import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.state.ReadUnitProperties;
import org.hage.platform.simulation.runtime.state.ReadWriteUnitProperties;

import java.util.List;
import java.util.function.Predicate;

public interface UnitPropertiesQueryingContext {

    /**
     * Query properties of specified unit.
     *
     * @param unitAddress address of unit for querying properties
     * @return actual unit properties of wanted unit or its empty implementation empty in case if unit address is not legal or cannot be
     * reached by invoking agent
     */
    ReadUnitProperties queryPropertiesOf(UnitAddress unitAddress);

    /**
     * Query properties of local to caller unit.
     *
     * @return unit properties for current local agent unit
     */
    ReadWriteUnitProperties queryLocalProperties();

    /**
     * Query for all surrounding units with properties which match given predicate.
     *
     * @param predicate predicate to match properties
     * @return addresses of units which have matching properties
     */
    List<UnitAddress> queryNeighborsWithMatchingProperties(Predicate<ReadUnitProperties> predicate);
}
