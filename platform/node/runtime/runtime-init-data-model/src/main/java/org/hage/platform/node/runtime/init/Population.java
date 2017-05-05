package org.hage.platform.node.runtime.init;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hage.platform.node.structure.Position;

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

// TODO: split model and functionallity
@Immutable
@EqualsAndHashCode
@ToString
public class Population implements Serializable {
    private static final Population EMPTY = new Population(emptyMap());

    private final Map<Position, UnitPopulation> distributionMap;

    private Population(Map<Position, UnitPopulation> distributionMap) {
        this.distributionMap = unmodifiableMap(distributionMap);
    }

    public static Population emptyPopulation() {
        return EMPTY;
    }

    public static Population populationFromMap(Map<Position, UnitPopulation> distributionMap) {
        return new Population(new HashMap<>(distributionMap));
    }

    public UnitPopulation unitPopulationFor(Position position) {
        return distributionMap.getOrDefault(position, UnitPopulation.emptyUnitPopulation());
    }

    public Set<Position> getInternalPositions() {
        return unmodifiableSet(distributionMap.keySet());
    }

    public List<Population> split(List<Set<Position>> splitPositionsGrouping) {
        return splitPositionsGrouping
            .stream()
            .map(internalPositions ->
                populationFromMap(
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
            .mapToLong(UnitPopulation::getAgentsCount)
            .sum();
    }

    public Population merge(Population otherMap) {
        if (distributionMap.isEmpty()) {
            return otherMap;
        }

        if (otherMap.distributionMap.isEmpty()) {
            return this;
        }

        HashMap<Position, UnitPopulation> mergeMap = new HashMap<>(distributionMap);

        for (Position position : otherMap.getInternalPositions()) {
            UnitPopulation unitPopulation = otherMap.unitPopulationFor(position);
            UnitPopulation existingUnitPopulation = mergeMap.getOrDefault(position, UnitPopulation.emptyUnitPopulation());

            mergeMap.put(position, existingUnitPopulation.merge(unitPopulation));
        }

        return populationFromMap(mergeMap);
    }
}
