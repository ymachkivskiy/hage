package org.hage.platform.component.runtime.init;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.*;

// TODO: split model and functionallity
@Immutable
@EqualsAndHashCode
@ToString
public class UnitPopulation implements Serializable {
    private static final UnitPopulation EMPTY = new UnitPopulation(emptyMap());
    private final Map<AgentDefinition, Integer> agentCountMap;

    private UnitPopulation(Map<AgentDefinition, Integer> agentCountMap) {
        this.agentCountMap = unmodifiableMap(new HashMap<>(agentCountMap));
    }

    public static UnitPopulation emptyUnitPopulation() {
        return EMPTY;
    }

    public static UnitPopulation populationFromMap(Map<AgentDefinition, Integer> agentCountMap) {
        return new UnitPopulation(agentCountMap);
    }

    public static UnitPopulation populationFromPair(AgentDefinition agentDefinition, Integer agentCount) {
        return new UnitPopulation(singletonMap(agentDefinition, agentCount));
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

    public UnitPopulation merge(UnitPopulation other) {
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
