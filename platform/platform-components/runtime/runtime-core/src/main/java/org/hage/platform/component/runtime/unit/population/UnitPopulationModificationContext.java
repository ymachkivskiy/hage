package org.hage.platform.component.runtime.unit.population;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.global.SimulationAgentDefinitionsSupplier;
import org.hage.platform.component.runtime.init.AgentDefinitionCount;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.util.StatefulPrototypeComponentsInitializer;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.context.AgentCreationContext;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Slf4j
@PrototypeComponent
@RequiredArgsConstructor
public class UnitPopulationModificationContext implements AgentCreationContext {

    private static final AgentInitializer<?> EMPTY_INITIALIZER = agent -> {
    };

    @Autowired
    private StatefulPrototypeComponentsInitializer statefulInitializer;
    @Autowired
    private SimulationAgentDefinitionsSupplier agentDefinitionsSupplier;

    private final MutableInstanceContainer instanceContainer;
    private final PopulationController populationController;

    @Override
    public Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        return agentDefinitionsSupplier.getSupportedAgentTypes();
    }

    @Override
    public <T extends Agent> void newAgent(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        newAgent(agentClazz, (AgentInitializer<T>) EMPTY_INITIALIZER);
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, int agentsNumber) throws UnsupportedAgentTypeException {
        newAgents(agentClazz, (AgentInitializer<T>) EMPTY_INITIALIZER, agentsNumber);
    }

    @Override
    public <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer) throws UnsupportedAgentTypeException {
        newAgents(agentClazz, initializer, 1);
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber) throws UnsupportedAgentTypeException {
        checkAgentClazz(agentClazz);

        Collection<T> agents = IntStream.range(0, agentsNumber)
            .mapToObj(i -> instanceContainer.getInstance(agentClazz))
            .peek(initializer::initAgent)
            .collect(toList());

        registerAgents(agents, false);
    }

    public void loadPopulation(UnitPopulation population) {
        log.debug("Configure with initial population {}", population);

        createControlAgent();
        createInitialAgents(population);
    }

    private void createControlAgent() {
        log.debug("Create control agent");

        agentDefinitionsSupplier.getControlAgentType()
            .map(instanceContainer::getInstance)
            .ifPresent(controlAgent -> {
                statefulInitializer.performInitialization(controlAgent);
                populationController.setControlAgent(controlAgent);
            });
    }


    private void createInitialAgents(UnitPopulation population) {
        log.debug("Create initial agents");

        List<Agent> agents = population.getCountedAgents().stream()
            .map(this::createNotInitializedAgents)
            .flatMap(List::stream)
            .collect(toList());

        registerAgents(agents, true);
    }

    private <T extends Agent> void checkAgentClazz(Class<T> agentClazz) {
        if (!agentDefinitionsSupplier.isSupportedAgent(agentClazz)) {
            throw new UnsupportedAgentTypeException(agentClazz);
        }
    }

    private <T extends Agent> void registerAgents(Collection<T> agents, boolean immediately) {
        statefulInitializer.performInitialization(agents);
        if (immediately) {
            populationController.addAgents(agents);
        } else {
            populationController.addAgentsToStepBuffer(agents);
        }
    }

    private List<Agent> createNotInitializedAgents(AgentDefinitionCount definitionCount) {
        Class<? extends Agent> agentClazz = definitionCount.getAgentDefinition().getAgentClass();
        checkAgentClazz(agentClazz);

        return range(0, definitionCount.getCount())
            .mapToObj(i -> instanceContainer.getInstance(agentClazz))
            .collect(toList());
    }

}
