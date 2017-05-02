package org.hage.platform.component.runtime.container;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.runtime.activepopulation.AgentsTargetEnvironment;
import org.hage.platform.component.runtime.init.AgentDefinitionCount;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.unit.UnitComponent;
import org.hage.platform.component.runtime.unit.UnitContainer;
import org.hage.platform.component.runtime.unit.UnitPopulationLoader;
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
public class UnitComponentCreationController implements UnitPopulationLoader, AgentsCreator, UnitComponent {

    private final AgentsTargetEnvironment agentsTargetEnvironment;

    @Setter
    private UnitContainer unitContainer;

    @Autowired
    private SimulationAgentDefinitionsSupplier agentDefinitionsSupplier;

    @Override
    public void loadPopulation(UnitPopulation population) {
        log.debug("Configure with initial population {}", population);
        createInitialAgents(population);
    }

    @Override
    public Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        return agentDefinitionsSupplier.getSupportedAgentTypes();
    }

    @Override
    public <T extends Agent> void checkAgentClazz(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        if (!agentDefinitionsSupplier.isSupportedAgent(agentClazz)) {
            throw new UnsupportedAgentTypeException(agentClazz);
        }
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber, boolean addImmediately) throws UnsupportedAgentTypeException {
        checkAgentClazz(agentClazz);
        checkArgument(agentsNumber > 0, "Agent number must be positive integer greater than zero");

        Collection<T> agents = IntStream.range(0, agentsNumber)
            .mapToObj(i -> unitContainer.newAgentInstance(agentClazz))
            .peek(initializer::initAgent)
            .collect(toList());

        registerAgents(agents, addImmediately);
    }

    @Override
    public void performPostConstruction() {
        //todo : NOT IMPLEMENTED

    }

    private <T extends Agent> void registerAgents(Collection<T> agents, boolean immediately) {
        if (immediately) {
            agentsTargetEnvironment.addAgentsImmediately(agents);
        } else {
            agentsTargetEnvironment.scheduleAddAgents(agents);
        }
    }

    private void createInitialAgents(UnitPopulation population) {
        log.debug("Create initial agents");

        List<Agent> agents = population.getCountedAgents().stream()
            .map(this::createAgentsForDefinition)
            .flatMap(List::stream)
            .collect(toList());

        registerAgents(agents, true);
    }

    private List<Agent> createAgentsForDefinition(AgentDefinitionCount definitionCount) {
        Class<? extends Agent> agentClazz = definitionCount.getAgentDefinition().getAgentClass();
        checkAgentClazz(agentClazz);

        return range(0, definitionCount.getCount())
            .mapToObj(i -> unitContainer.newAgentInstance(agentClazz))
            .collect(toList());
    }

}
