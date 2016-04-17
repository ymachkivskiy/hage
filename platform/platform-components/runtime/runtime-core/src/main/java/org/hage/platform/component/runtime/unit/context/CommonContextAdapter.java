package org.hage.platform.component.runtime.unit.context;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationModificationContext;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.context.AgentCreationContext;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.LocationContext;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;

import java.util.Set;

@RequiredArgsConstructor
public class CommonContextAdapter implements LocationContext, AgentCreationContext {

    private final UnitLocationContext unitLocationContext;
    private final UnitPopulationModificationContext unitPopulationModificationContext;

    @Override
    public final Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        return unitPopulationModificationContext.getSupportedAgentsTypes();
    }

    @Override
    public final <T extends Agent> void newAgent(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        unitPopulationModificationContext.newAgent(agentClazz);
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, int agentsNumber) throws UnsupportedAgentTypeException {
        unitPopulationModificationContext.newAgents(agentClazz, agentsNumber);
    }

    @Override
    public final <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer) throws UnsupportedAgentTypeException {
        unitPopulationModificationContext.newAgent(agentClazz, initializer);
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber) throws UnsupportedAgentTypeException {
        unitPopulationModificationContext.newAgents(agentClazz, initializer, agentsNumber);
    }

    @Override
    public final UnitAddress queryLocalUnit() {
        return unitLocationContext.queryLocalUnit();
    }

    @Override
    public final Neighbors querySurroundingUnits() {
        return unitLocationContext.querySurroundingUnits();
    }
}
