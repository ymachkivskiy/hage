package org.hage.platform.node.runtime.location;

import com.google.common.base.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.cluster.api.LocalClusterNode;
import org.hage.platform.node.runtime.unit.UnitComponent;
import org.hage.platform.node.runtime.unit.UnitContainer;
import org.hage.platform.node.structure.Position;
import org.hage.platform.node.structure.connections.Neighbors;
import org.hage.platform.node.structure.connections.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.base.Suppliers.memoize;

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
        unitAddressSupplier = memoize(() -> AgentsUnitAddress.onlineAddress(position, localClusterNode.getLocalAddress()));
    }


    @Override
    public String toString() {
        return getUniqueIdentifier();
    }
}
