package org.hage.platform.config;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hage.platform.habitat.AgentDefinition;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.*;

@Immutable
@EqualsAndHashCode
@ToString
public class CellPopulationDescription implements Serializable {
    private static final CellPopulationDescription EMPTY = new CellPopulationDescription(emptyMap());

    private final Map<AgentDefinition, Integer> agentCountMap;

    private CellPopulationDescription(Map<AgentDefinition, Integer> agentCountMap) {
        this.agentCountMap = unmodifiableMap(agentCountMap);
    }

    public static CellPopulationDescription emptyPopulation() {
        return EMPTY;
    }

    public static CellPopulationDescription populationFromMap(Map<AgentDefinition, Integer> agentCountMap) {
        return new CellPopulationDescription(agentCountMap);
    }

    public static CellPopulationDescription populationFromPair(AgentDefinition agentDefinition, Integer agentCount) {
        return new CellPopulationDescription(singletonMap(agentDefinition, agentCount));
    }

    public List<AgentDefinition> getAgentDefinitions() {
        return new ArrayList<>(agentCountMap.keySet());
    }

    public int getAgentCountForDefinition(AgentDefinition definition) {
        return agentCountMap.getOrDefault(definition, 0);
    }

    public int getAgentsCount() {
        return agentCountMap.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public CellPopulationDescription merge(CellPopulationDescription other) {
        if (this.getAgentDefinitions().isEmpty()) {
            return other;
        }
        if (other.getAgentDefinitions().isEmpty()) {
            return this;
        }

        Map<AgentDefinition, Integer> mergingMap = new HashMap<>(agentCountMap);

        for (AgentDefinition agentDefinition : other.getAgentDefinitions()) {
            int count = other.getAgentCountForDefinition(agentDefinition);
            Integer currentCount = mergingMap.getOrDefault(agentDefinition, 0);
            mergingMap.put(agentDefinition, currentCount + count);
        }

        return populationFromMap(mergingMap);
    }

    public boolean isEmpty() {
        return agentCountMap.isEmpty();
    }
}
