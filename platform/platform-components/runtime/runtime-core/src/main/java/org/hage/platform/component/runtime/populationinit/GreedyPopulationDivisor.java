package org.hage.platform.component.runtime.populationinit;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.structure.Position;
import org.hage.util.proportion.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.util.stream.Collector.of;
import static java.util.stream.Collectors.toList;

public class GreedyPopulationDivisor implements ProportionsDivisor<Population> {

    @Override
    public Division<Population> divideUsingProportions(Population source, Proportions proportions) {

        BigDecimal numberOfAgents = BigDecimal.valueOf(source.getNumberOfAgents());

        List<NumberForCountable> acquiredNumbersOfAgents = proportions.getElements()
            .stream()
            .map(countable -> new NumberForCountable(
                countable,
                proportions.getFractionFor(countable).multiply(numberOfAgents).setScale(0, ROUND_CEILING).longValue()
            ))
            .sorted((a, b) -> -a.compareTo(b))
            .collect(toList());

        Stack<NumberForInternalPosition> populationsPerCell = source.getInternalPositions()
            .stream()
            .map(ip -> new NumberForInternalPosition(ip, source.unitPopulationFor(ip)))
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

        List<Population> splittedConfigList = source.split(splitList);

        List<DivisionPart<Population>> resultDivision = new ArrayList<>();

        for (int i = 0; i < acquiredNumbersOfAgents.size(); i++) {
            Countable countable = acquiredNumbersOfAgents.get(i).getCountable();
            Population population = splittedConfigList.get(i);
            resultDivision.add(new DivisionPart<>(countable, population));
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

        public NumberForInternalPosition(Position position, UnitPopulation unitPopulation) {
            this.position = position;
            this.number = unitPopulation.getAgentsCount();
        }

        @Override
        public int compareTo(NumberForInternalPosition o) {
            return number - o.number;
        }
    }
}
