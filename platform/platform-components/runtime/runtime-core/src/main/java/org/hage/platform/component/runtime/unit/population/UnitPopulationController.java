package org.hage.platform.component.runtime.unit.population;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.util.StatefulPrototypeComponentsInitializer;
import org.hage.platform.simulation.runtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UnitPopulationController {

    @Autowired
    private StatefulPrototypeComponentsInitializer statefulInitializer;

    private final MutableInstanceContainer instanceContainer;

    public InitialPopulation configureWithUnitPopulation(UnitPopulation population) {
        List<Agent> agents = new ArrayList<>(population.getAgentsCount());

        for (AgentDefinition agentDefinition : population.getAgentDefinitions()) {
            instanceContainer.addPrototypeComponent(agentDefinition.getAgentClass());
            agents.addAll(createNotInitializedAgents(agentDefinition, population.getAgentCountForDefinition(agentDefinition)));
        }

        statefulInitializer.performInitialization(agents);

        return new InitialPopulation(empty(), agents);
    }

    public Agent createAgent(AgentDefinition definition) {
        // TODO: make some check if definition is registered
        Agent agent = instanceContainer.getInstance(definition.getAgentClass());
        statefulInitializer.performInitialization(agent);
        return agent;
    }

    private List<Agent> createNotInitializedAgents(AgentDefinition definition, int count) {
        return range(0, count)
            .mapToObj(i -> instanceContainer.getInstance(definition.getAgentClass()))
            .collect(toList());
    }

}
