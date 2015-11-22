package org.hage.platform.config.def;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hage.platform.habitat.structure.InternalPosition;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;

@Immutable
@EqualsAndHashCode
@ToString
public class PopulationDistributionMap {
    private static final PopulationDistributionMap EMPTY = new PopulationDistributionMap(emptyMap());

    private final Map<InternalPosition, CellPopulationDescription> distributionMap;

    private PopulationDistributionMap(Map<InternalPosition, CellPopulationDescription> distributionMap) {
        this.distributionMap = unmodifiableMap(distributionMap);
    }

    public CellPopulationDescription getPopulationFor(InternalPosition internalPosition) {
        return distributionMap.getOrDefault(internalPosition, CellPopulationDescription.empty());
    }

    public Set<InternalPosition> getInternalPositions() {
        return unmodifiableSet(distributionMap.keySet());
    }

    public List<PopulationDistributionMap> split(List<Set<InternalPosition>> splitPositionsGrouping) {
        return splitPositionsGrouping.stream().map(s -> empty()).collect(toList());
    }

    public PopulationDistributionMap merge(PopulationDistributionMap otherMap) {
        return new PopulationDistributionMap(emptyMap());
    }

    public static PopulationDistributionMap empty() {
        return EMPTY;
    }

    public static PopulationDistributionMap fromMap(Map<InternalPosition, CellPopulationDescription> distributionMap) {
        return new PopulationDistributionMap(distributionMap);
    }
}
