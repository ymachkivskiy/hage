package org.hage.platform.config.division;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.config.CellPopulationDescription;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.HabitatGeography;
import org.hage.platform.config.PopulationDistributionMap;
import org.hage.platform.habitat.structure.InternalPosition;
import org.hage.util.proportion.Countable;
import org.hage.util.proportion.Proportions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.util.stream.Collector.of;
import static java.util.stream.Collectors.toList;

@Component
public class SimpleConfigurationSplitter {

    public ConfigurationDivision split(ComputationConfiguration configuration, Proportions proportions) {
        PopulationDistributionMap populationDistributionMap = configuration.getHabitatGeography().getPopulationDistributionMap();
        BigDecimal numberOfAgents = BigDecimal.valueOf(populationDistributionMap.getNumberOfAgents());

        List<NumberForCountable> acquiredNumbersOfAgents = proportions.getElements()
            .stream()
            .map(countable -> new NumberForCountable(
                countable,
                proportions.getFractionFor(countable).multiply(numberOfAgents).setScale(0, ROUND_CEILING).longValue()
            ))
            .sorted((a, b) -> -a.compareTo(b))
            .collect(toList());

        Stack<NumberForInternalPosition> populationsPerCell = populationDistributionMap.getInternalPositions()
            .stream()
            .map(ip -> new NumberForInternalPosition(ip, populationDistributionMap.getPopulationFor(ip)))
            .sorted()
            .collect(toStack());

        List<Set<InternalPosition>> splitList = new ArrayList<>();
        for (NumberForCountable acquiredNumbersOfAgent : acquiredNumbersOfAgents) {
            long currentAcc = 0;
            Set<InternalPosition> currentGroup = new HashSet<>();

            while (currentAcc < acquiredNumbersOfAgent.getNumber() && !populationsPerCell.isEmpty()) {
                final NumberForInternalPosition numberForInternalPosition = populationsPerCell.pop();
                currentGroup.add(numberForInternalPosition.getInternalPosition());
                currentAcc += numberForInternalPosition.getNumber();
            }

            splitList.add(currentGroup);
        }

        Map<Countable, ComputationConfiguration> computationMap = new HashMap<>();
        List<PopulationDistributionMap> splittedConfigList = populationDistributionMap.split(splitList);

        for (int i = 0; i < acquiredNumbersOfAgents.size(); i++) {
            Countable countable = acquiredNumbersOfAgents.get(i).getCountable();
            ComputationConfiguration compConfig = ComputationConfiguration.builder()
                .ratingConfig(configuration.getRatingConfig())
                .globalComponents(configuration.getGlobalComponents())
                .habitatGeography(new HabitatGeography(configuration.getHabitatGeography().getStructureDefinition(), splittedConfigList.get(i)))
                .build();
            computationMap.put(countable, compConfig);
        }

        return new ConfigurationDivision(computationMap);
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
        private final InternalPosition internalPosition;
        @Getter
        private final int number;

        public NumberForInternalPosition(InternalPosition internalPosition, CellPopulationDescription cellPopulationDescription) {
            this.internalPosition = internalPosition;
            this.number = cellPopulationDescription.getAgentsCount();
        }

        @Override
        public int compareTo(NumberForInternalPosition o) {
            return number - o.number;
        }
    }

}
