package org.hage.platform.component.simulation.structure;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.simulation.structure.definition.CellPopulation;
import org.hage.platform.component.simulation.structure.definition.Population;
import org.hage.platform.component.simulation.structure.definition.Position;
import org.hage.util.proportion.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.util.stream.Collector.of;
import static java.util.stream.Collectors.toList;

@Component
public class SimpleOrganizationDivisor implements ProportionsDivisor<SimulationOrganization> {

    @Override
    public Division<SimulationOrganization> divideUsingProportions(SimulationOrganization source, Proportions proportions) {

        Population sourcePopulationDistrMap = source.getPopulation();
        BigDecimal numberOfAgents = BigDecimal.valueOf(sourcePopulationDistrMap.getNumberOfAgents());

        List<NumberForCountable> acquiredNumbersOfAgents = proportions.getElements()
            .stream()
            .map(countable -> new NumberForCountable(
                countable,
                proportions.getFractionFor(countable).multiply(numberOfAgents).setScale(0, ROUND_CEILING).longValue()
            ))
            .sorted((a, b) -> -a.compareTo(b))
            .collect(toList());

        Stack<NumberForInternalPosition> populationsPerCell = sourcePopulationDistrMap.getInternalPositions()
            .stream()
            .map(ip -> new NumberForInternalPosition(ip, sourcePopulationDistrMap.getPopulationFor(ip)))
            .sorted()
            .collect(toStack());

        List<Set<Position>> splitList = new ArrayList<>();
        for (NumberForCountable acquiredNumbersOfAgent : acquiredNumbersOfAgents) {
            long currentAcc = 0;
            Set<Position> currentGroup = new HashSet<>();

            while (currentAcc < acquiredNumbersOfAgent.getNumber() && !populationsPerCell.isEmpty()) {
                final NumberForInternalPosition numberForInternalPosition = populationsPerCell.pop();
                currentGroup.add(numberForInternalPosition.getPosition());
                currentAcc += numberForInternalPosition.getNumber();
            }

            splitList.add(currentGroup);
        }

        List<Population> splittedConfigList = sourcePopulationDistrMap.split(splitList);

        List<DivisionPart<SimulationOrganization>> resultDivision = new ArrayList<>();

        for (int i = 0; i < acquiredNumbersOfAgents.size(); i++) {
            Countable countable = acquiredNumbersOfAgents.get(i).getCountable();
            SimulationOrganization simulationOrganization = new SimulationOrganization(source.getStructureDefinition(), splittedConfigList.get(i));
            DivisionPart<SimulationOrganization> p = new DivisionPart<>(countable, simulationOrganization);
            resultDivision.add(p);
        }

        return new Division<>(resultDivision);
    }


    private static Collector<NumberForInternalPosition, Stack<NumberForInternalPosition>, Stack<NumberForInternalPosition>> toStack() {
        return of(
            Stack::new,
            Stack::add,
            (f, s) -> {
                f.addAll(s);
                return f;
            }
        );
    }


    @Data
    private static class NumberForCountable implements Comparable<NumberForCountable> {
        private final Countable countable;
        private final Long number;

        @Override
        public int compareTo(NumberForCountable o) {
            return number.compareTo(o.number);
        }
    }

    @ToString
    private static class NumberForInternalPosition implements Comparable<NumberForInternalPosition> {
        @Getter
        private final Position position;
        @Getter
        private final int number;

        public NumberForInternalPosition(Position position, CellPopulation cellPopulation) {
            this.position = position;
            this.number = cellPopulation.getAgentsCount();
        }

        @Override
        public int compareTo(NumberForInternalPosition o) {
            return number - o.number;
        }
    }
}
