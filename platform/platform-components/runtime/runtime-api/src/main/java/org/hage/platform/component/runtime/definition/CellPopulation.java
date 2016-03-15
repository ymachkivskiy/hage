package org.hage.platform.component.runtime.definition;

import lombok.EqualsAndHashCode;
import lombok.ToString;

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
public class CellPopulation implements Serializable {
    private static final CellPopulation EMPTY = new CellPopulation(emptyMap());
    private final Map<AgentDefinition, Integer> agentCountMap;

    private CellPopulation(Map<AgentDefinition, Integer> agentCountMap) {
        this.agentCountMap = unmodifiableMap(new HashMap<>(agentCountMap));
    }

    public static CellPopulation emptyPopulation() {
        return EMPTY;
    }

    public static CellPopulation populationFromMap(Map<AgentDefinition, Integer> agentCountMap) {
        return new CellPopulation(agentCountMap);
    }

    public static CellPopulation populationFromPair(AgentDefinition agentDefinition, Integer agentCount) {
        return new CellPopulation(singletonMap(agentDefinition, agentCount));
    }

    public List<AgentDefinition> getAgents() {
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

    public CellPopulation merge(CellPopulation other) {
        if (this.getAgents().isEmpty()) {
            return other;
        }
        if (other.getAgents().isEmpty()) {
            return this;
        }

        Map<AgentDefinition, Integer> mergingMap = new HashMap<>(agentCountMap);

        for (AgentDefinition agentDefinition : other.getAgents()) {
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
