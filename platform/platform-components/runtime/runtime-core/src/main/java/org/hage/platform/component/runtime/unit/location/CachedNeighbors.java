package org.hage.platform.component.runtime.unit.location;

import com.google.common.base.Supplier;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.RelativePosition;
import org.hage.platform.component.structure.connections.UnitAddress;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Suppliers.memoize;
import static java.util.EnumSet.of;
import static java.util.stream.Collectors.toList;

class CachedNeighbors implements Neighbors {

    private final Map<RelativePosition, Set<Position>> positioningMap;
    private final Map<Position, AgentsUnitAddress> addressMap;

    private final Supplier<List<UnitAddress>> allAddressesSupplier;
    private final ConcurrentMap<EnumSet<RelativePosition>, List<UnitAddress>> neighborhoodCache = new ConcurrentHashMap<>();


    public CachedNeighbors(Map<Position, AgentsUnitAddress> addressMap, Map<RelativePosition, Set<Position>> positioningMap) {
        this.addressMap = new HashMap<>(addressMap);
        this.positioningMap = new EnumMap<>(positioningMap);
        this.allAddressesSupplier = memoize(() -> new ArrayList<>(addressMap.values()));
    }


    @Override
    public List<UnitAddress> getAll() {
        return allAddressesSupplier.get();
    }

    @Override
    public List<UnitAddress> getLocated(RelativePosition firstRelativeSelector, RelativePosition... otherSelectors) {
        return neighborhoodCache.computeIfAbsent(of(firstRelativeSelector, otherSelectors), this::computeNeighbours);
    }

    private List<UnitAddress> computeNeighbours(EnumSet<RelativePosition> relativePositions) {
        Set<Position> commonPositions = getCommonPositions(relativePositions);
        return translatePositionsToAddresses(commonPositions);
    }

    private Set<Position> getCommonPositions(EnumSet<RelativePosition> relativePositions) {
        return relativePositions.stream()
            .map(positioningMap::get)
            .reduce(
                new HashSet<>(addressMap.keySet()),
                (acc, currPos) -> {
                    acc.retainAll(currPos);
                    return acc;
                }
            );
    }

    private List<UnitAddress> translatePositionsToAddresses(Set<Position> commonPositions) {
        return commonPositions.stream()
            .map(addressMap::get)
            .collect(toList());
    }


}
