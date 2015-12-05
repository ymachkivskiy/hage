package org.hage.platform.config.def;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hage.platform.habitat.structure.InternalPosition;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Immutable
@EqualsAndHashCode
@ToString
public class PopulationDistributionMap implements Serializable {
    private static final PopulationDistributionMap EMPTY = new PopulationDistributionMap(emptyMap());

    private final Map<InternalPosition, CellPopulationDescription> distributionMap;

    private PopulationDistributionMap(Map<InternalPosition, CellPopulationDescription> distributionMap) {
        this.distributionMap = unmodifiableMap(distributionMap);
    }

    public static PopulationDistributionMap emptyDistributionMap() {
        return EMPTY;
    }

    public static PopulationDistributionMap distributionFromMap(Map<InternalPosition, CellPopulationDescription> distributionMap) {
        return new PopulationDistributionMap(new HashMap<>(distributionMap));
    }

    public CellPopulationDescription getPopulationFor(InternalPosition internalPosition) {
        return distributionMap.getOrDefault(internalPosition, CellPopulationDescription.emptyPopulation());
    }

    public Set<InternalPosition> getInternalPositions() {
        return unmodifiableSet(distributionMap.keySet());
    }

    public List<PopulationDistributionMap> split(List<Set<InternalPosition>> splitPositionsGrouping) {
        return splitPositionsGrouping
            .stream()
            .map(internalPositions ->
                distributionFromMap(
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

    public Long getNumberOfAgents(){
        return distributionMap.values()
            .stream()
            .mapToLong(CellPopulationDescription::getAgentsCount)
            .sum();
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
            CellPopulationDescription existingCellPopulation = mergeMap.getOrDefault(internalPosition, CellPopulationDescription.emptyPopulation());

            mergeMap.put(internalPosition, existingCellPopulation.merge(cellPopulation));
        }

        return distributionFromMap(mergeMap);
    }
}
