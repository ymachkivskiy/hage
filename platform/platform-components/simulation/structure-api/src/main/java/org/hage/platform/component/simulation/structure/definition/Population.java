package org.hage.platform.component.simulation.structure.definition;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Immutable
@EqualsAndHashCode
@ToString
public class Population implements Serializable {
    private static final Population EMPTY = new Population(emptyMap());

    private final Map<Position, CellPopulation> distributionMap;

    private Population(Map<Position, CellPopulation> distributionMap) {
        this.distributionMap = unmodifiableMap(distributionMap);
    }

    public static Population emptyDistributionMap() {
        return EMPTY;
    }

    public static Population distributionFromMap(Map<Position, CellPopulation> distributionMap) {
        return new Population(new HashMap<>(distributionMap));
    }

    public CellPopulation getPopulationFor(Position position) {
        return distributionMap.getOrDefault(position, CellPopulation.emptyPopulation());
    }

    public Set<Position> getInternalPositions() {
        return unmodifiableSet(distributionMap.keySet());
    }

    public List<Population> split(List<Set<Position>> splitPositionsGrouping) {
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
            .mapToLong(CellPopulation::getAgentsCount)
            .sum();
    }

    public Population merge(Population otherMap) {
        if (distributionMap.isEmpty()) {
            return otherMap;
        }

        if (otherMap.distributionMap.isEmpty()) {
            return this;
        }

        HashMap<Position, CellPopulation> mergeMap = new HashMap<>(distributionMap);

        for (Position position : otherMap.getInternalPositions()) {
            CellPopulation cellPopulation = otherMap.getPopulationFor(position);
            CellPopulation existingCellPopulation = mergeMap.getOrDefault(position, CellPopulation.emptyPopulation());

            mergeMap.put(position, existingCellPopulation.merge(cellPopulation));
        }

        return distributionFromMap(mergeMap);
    }
}
