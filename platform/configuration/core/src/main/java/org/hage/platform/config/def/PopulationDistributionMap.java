package org.hage.platform.config.def;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hage.platform.habitat.structure.InternalPosition;

import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Immutable
@EqualsAndHashCode
@ToString
public class PopulationDistributionMap {
    private static final PopulationDistributionMap EMPTY = new PopulationDistributionMap(emptyMap());

    private final Map<InternalPosition, CellPopulationDescription> distributionMap;

    private PopulationDistributionMap(Map<InternalPosition, CellPopulationDescription> distributionMap) {
        this.distributionMap = unmodifiableMap(distributionMap);
    }

    public static PopulationDistributionMap empty() {
        return EMPTY;
    }

    public static PopulationDistributionMap fromMap(Map<InternalPosition, CellPopulationDescription> distributionMap) {
        return new PopulationDistributionMap(distributionMap);
    }

    public CellPopulationDescription getPopulationFor(InternalPosition internalPosition) {
        return distributionMap.getOrDefault(internalPosition, CellPopulationDescription.empty());
    }

    public Set<InternalPosition> getInternalPositions() {
        return unmodifiableSet(distributionMap.keySet());
    }

    public List<PopulationDistributionMap> split(List<Set<InternalPosition>> splitPositionsGrouping) {
        return splitPositionsGrouping
            .stream()
            .map(internalPositions ->
                fromMap(
                    internalPositions
                        .stream()
                        .collect(toMap(
                            Function.identity(),
                            distributionMap::get
                        ))
                )
            )
            .collect(toList());
    }

    public PopulationDistributionMap merge(PopulationDistributionMap otherMap) {
        if (distributionMap.isEmpty()) {
            return otherMap;
        }

        if (otherMap.distributionMap.isEmpty()) {
            return this;
        }

        HashMap<InternalPosition, CellPopulationDescription> mergeMap = new HashMap<>(distributionMap);

        for (InternalPosition internalPosition : otherMap.getInternalPositions()) {
            CellPopulationDescription cellPopulation = otherMap.getPopulationFor(internalPosition);
            CellPopulationDescription existingCellPopulation = mergeMap.getOrDefault(internalPosition, CellPopulationDescription.empty());

            mergeMap.put(internalPosition, existingCellPopulation.merge(cellPopulation));
        }

        return fromMap(mergeMap);
    }
}
