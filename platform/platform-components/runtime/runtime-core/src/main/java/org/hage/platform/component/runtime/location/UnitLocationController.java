package org.hage.platform.component.runtime.location;

import com.google.common.base.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.cluster.LocalClusterNode;
import org.hage.platform.component.runtime.unit.UnitComponent;
import org.hage.platform.component.runtime.unit.UnitContainer;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.base.Suppliers.memoize;
import static org.hage.platform.component.runtime.location.AgentsUnitAddress.onlineAddress;

@Slf4j
@RequiredArgsConstructor
@PrototypeComponent
public class UnitLocationController implements UnitComponent {

    @Getter
    private final Position position;
    @Setter
    private UnitContainer unitContainer;

    @Autowired
    private LocalClusterNode localClusterNode;
    @Autowired
    private NeighborsResolver neighborsResolver;
    @Autowired
    private Structure structure;

    private Supplier<Neighbors> neighborsSupplier;
    private Supplier<AgentsUnitAddress> unitAddressSupplier;

    public boolean isLegalMigrationTargetAddress(AgentsUnitAddress unitAddress) {
        return !queryLocalUnit().equals(unitAddress)
            && structure.belongsToStructure(unitAddress.getPosition());
    }

    public AgentsUnitAddress queryLocalUnit() {
        return unitAddressSupplier.get();
    }

    public Neighbors querySurroundingUnits() {
        return neighborsSupplier.get();
    }

    public String getUniqueIdentifier() {
        return position.toString() + "[" + localClusterNode.getLocalAddress().getFriendlyIdentifier() + "]";
    }

    public void performPostProcessing() {
        reset();
    }

    @Override
    public void performPostConstruction() {
        reset();
    }

    private void reset() {
        log.debug("Reset location context for {}", position);
        neighborsSupplier = memoize(() -> neighborsResolver.resolveForPosition(position));
        unitAddressSupplier = memoize(() -> onlineAddress(position, localClusterNode.getLocalAddress()));
    }


    @Override
    public String toString() {
        return getUniqueIdentifier();
    }
}
