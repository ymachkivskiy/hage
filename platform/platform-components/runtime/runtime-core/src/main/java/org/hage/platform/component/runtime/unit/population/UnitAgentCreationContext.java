package org.hage.platform.component.runtime.unit.population;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.global.SimulationAgentDefinitionsSupplier;
import org.hage.platform.component.runtime.init.AgentDefinitionCount;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Slf4j
@PrototypeComponent
@RequiredArgsConstructor
public class UnitAgentCreationContext {

    private static final AgentInitializer<?> EMPTY_INITIALIZER = agent -> {
    };

    @Autowired
    private SimulationAgentDefinitionsSupplier agentDefinitionsSupplier;

    private final MutableInstanceContainer instanceContainer;
    private final UnitActivePopulationController unitActivePopulationController;

    public Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        return agentDefinitionsSupplier.getSupportedAgentTypes();
    }

    public <T extends Agent> void newAgent(Class<T> agentClazz, boolean addImmediately) throws UnsupportedAgentTypeException {
        newAgent(agentClazz, (AgentInitializer<T>) EMPTY_INITIALIZER, addImmediately);
    }

    public <T extends Agent> void newAgents(Class<T> agentClazz, int agentsNumber, boolean addImmediately) throws UnsupportedAgentTypeException {
        newAgents(agentClazz, (AgentInitializer<T>) EMPTY_INITIALIZER, agentsNumber, addImmediately);
    }

    public <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer, boolean addImmediately) throws UnsupportedAgentTypeException {
        newAgents(agentClazz, initializer, 1, addImmediately);
    }

    public <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber, boolean addImmediately) throws UnsupportedAgentTypeException {
        checkAgentClazz(agentClazz);
        checkArgument(agentsNumber > 0, "Agent number must be positive integer greater than zero");

        Collection<T> agents = IntStream.range(0, agentsNumber)
            .mapToObj(i -> instanceContainer.getInstance(agentClazz))
            .peek(initializer::initAgent)
            .collect(toList());

        registerAgents(agents, addImmediately);
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
            .ifPresent(unitActivePopulationController::setControlAgent);
    }


    private void createInitialAgents(UnitPopulation population) {
        log.debug("Create initial agents");

        List<Agent> agents = population.getCountedAgents().stream()
            .map(this::createNotInitializedAgents)
            .flatMap(List::stream)
            .collect(toList());

        registerAgents(agents, true);
    }

    public <T extends Agent> void checkAgentClazz(Class<T> agentClazz) {
        if (!agentDefinitionsSupplier.isSupportedAgent(agentClazz)) {
            throw new UnsupportedAgentTypeException(agentClazz);
        }
    }

    private <T extends Agent> void registerAgents(Collection<T> agents, boolean immediately) {
        if (immediately) {
            unitActivePopulationController.addInstancesImmediately(agents);
        } else {
            unitActivePopulationController.scheduleAddInstances(agents);
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
