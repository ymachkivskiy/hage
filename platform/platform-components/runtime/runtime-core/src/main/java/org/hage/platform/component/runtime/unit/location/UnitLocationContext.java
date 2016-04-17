package org.hage.platform.component.runtime.unit.location;

import com.google.common.base.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.cluster.LocalNodeAddressSupplier;
import org.hage.platform.component.runtime.unit.UnitStepCycleAware;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static com.google.common.base.Suppliers.memoize;

@Slf4j
@RequiredArgsConstructor
@PrototypeComponent
public class UnitLocationContext implements LocationContext, UnitStepCycleAware {

    private final Position position;

    @Autowired
    private LocalNodeAddressSupplier localNodeAddressSupplier;
    @Autowired
    private NeighborsResolver neighborsResolver;

    private Supplier<Neighbors> neighborsSupplier;
    private Supplier<UnitAddress> unitAddressSupplier;


    @Override
    public UnitAddress queryLocalUnit() {
        return unitAddressSupplier.get();
    }

    @Override
    public Neighbors querySurroundingUnits() {
        return neighborsSupplier.get();
    }

    public String getUniqueIdentifier() {
        return position.toString() + "[" + localNodeAddressSupplier.getLocalAddress().getUniqueIdentifier() + "]";
    }

    @Override
    public void afterStepPerformed() {
        reset();
    }

    @PostConstruct
    private void init() {
        reset();
    }

    private void reset() {
        log.debug("Reset location context for {}", position);
        neighborsSupplier = memoize(() -> neighborsResolver.resolveForPosition(position));
        unitAddressSupplier = memoize(() -> new AgentsUnitAddress(localNodeAddressSupplier.getLocalAddress(), position));
    }


    @Override
    public String toString() {
        return getUniqueIdentifier();
    }
}
