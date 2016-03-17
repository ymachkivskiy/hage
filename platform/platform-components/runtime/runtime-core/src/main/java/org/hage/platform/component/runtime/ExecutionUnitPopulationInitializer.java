package org.hage.platform.component.runtime;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.agent.AgentAdapter;
import org.hage.platform.component.runtime.definition.AgentDefinition;
import org.hage.platform.component.runtime.definition.UnitPopulation;
import org.hage.platform.component.runtime.util.StatefulComponentsInitializer;
import org.hage.platform.simulation.base.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class ExecutionUnitPopulationInitializer {

    @Autowired
    private MutableInstanceContainer instanceContainer;
    @Autowired
    private StatefulComponentsInitializer statefulInitializer;
    @Autowired
    private AgentAdapterCreator adapterCreator;

    public void initializeUnitPopulation(BaseExecutionUnit executionUnit, UnitPopulation population) {

        MutableInstanceContainer unitContainer = instanceContainer.newChildContainer();

        registerDefinitions(unitContainer, population);
        List<Agent> agents = getAgentInstances(unitContainer, population);

        executionUnit.registerAgentAdapters(createAdaptersFor(agents));
        statefulInitializer.performInitialization(agents);

        executionUnit.setInstanceContainer(unitContainer);
    }

    private List<AgentAdapter> createAdaptersFor(List<Agent> agents) {
        return agents.stream().map(adapterCreator::createAdapterFor).collect(toList());
    }


    private void registerDefinitions(MutableInstanceContainer unitContainer, UnitPopulation population) {
        for (AgentDefinition agentDefinition : population.getAgentDefinitions()) {
            unitContainer.addPrototypeComponent(agentDefinition.getAgentClass());
        }
    }

    private List<Agent> getAgentInstances(MutableInstanceContainer unitContainer, UnitPopulation population) {

        List<Agent> instances = new ArrayList<>(population.getAgentsCount());

        for (AgentDefinition agentDefinition : population.getAgentDefinitions()) {
            int currentAgentCount = population.getAgentCountForDefinition(agentDefinition);
            for (int i = 0; i < currentAgentCount; i++) {
                instances.add(unitContainer.getInstance(agentDefinition.getAgentClass()));
            }
        }

        return instances;
    }


}
