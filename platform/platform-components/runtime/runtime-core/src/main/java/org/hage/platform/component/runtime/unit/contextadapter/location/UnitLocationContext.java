package org.hage.platform.component.runtime.unit.contextadapter.location;

import com.google.common.base.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.cluster.LocalNodeAddressSupplier;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static com.google.common.base.Suppliers.memoize;

@Slf4j
@PrototypeComponent
public class UnitLocationContext implements LocationContext {

    private final Position position;

    @Autowired
    private LocalNodeAddressSupplier localNodeAddressSupplier;
    @Autowired
    private NeighborsResolver neighborsResolver;

    private Supplier<Neighbors> neighborsSupplier;
    private Supplier<UnitAddress> unitAddressSupplier;


    public UnitLocationContext(Position position) {
        this.position = position;
    }

    @Override
    public UnitAddress queryLocalUnit() {
        return unitAddressSupplier.get();
    }

    @Override
    public Neighbors querySurroundingUnits() {
        return neighborsSupplier.get();
    }


    public void reset() {
        log.debug("Reset location context for {}", position);
        neighborsSupplier = memoize(() -> neighborsResolver.resolveForPosition(position));
        unitAddressSupplier = memoize(() -> new AgentsUnitAddress(localNodeAddressSupplier.getLocalAddress(), position));
    }

    @PostConstruct
    private void init() {
        reset();
    }

}
