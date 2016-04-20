package org.hage.platform.component.runtime.unit.agentcontext;

import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.component.runtime.unit.population.UnitActivePopulationController;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;
import org.hage.platform.simulation.runtime.control.AddressedAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.stream.Collectors.toList;

public class UnitAgentContextAdapter implements AgentManageContext, ControlAgentManageContext {

    private final UnitLocationContext locationCtxt;
    private final UnitAgentCreationContext creationCtxt;
    private final UnitActivePopulationController populationController;

    private AgentAdapter currentAgentContext;

    public UnitAgentContextAdapter(UnitLocationContext locationCtxt, UnitAgentCreationContext populationCtx, UnitActivePopulationController populationController) {
        this.locationCtxt = locationCtxt;
        this.creationCtxt = populationCtx;
        this.populationController = populationController;
    }

    @Override
    public final Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        return creationCtxt.getSupportedAgentsTypes();
    }

    @Override
    public final <T extends Agent> void newAgent(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        creationCtxt.newAgent(agentClazz, isControllAgentContext());
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, int agentsNumber) throws UnsupportedAgentTypeException {
        creationCtxt.newAgents(agentClazz, agentsNumber, isControllAgentContext());
    }

    @Override
    public final <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer) throws UnsupportedAgentTypeException {
        creationCtxt.newAgent(agentClazz, initializer, isControllAgentContext());
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber) throws UnsupportedAgentTypeException {
        creationCtxt.newAgents(agentClazz, initializer, agentsNumber, isControllAgentContext());
    }

    @Override
    public final UnitAddress queryLocalUnit() {
        return locationCtxt.queryLocalUnit();
    }

    @Override
    public final Neighbors querySurroundingUnits() {
        return locationCtxt.querySurroundingUnits();
    }

    @Override
    public <T extends Agent> List<AddressedAgent<T>> queryAgentsOfType(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        creationCtxt.checkAgentClazz(agentClazz);
        return populationController.getAdaptersForAgentsOfType(agentClazz).stream()
            .map(agentAdapter -> new AddressedAgent<>((T) agentAdapter.getAgent(), agentAdapter))
            .collect(toList());
    }

    @Override
    public boolean killAgent(AgentAddress agentAddress) {
        return agentAddress instanceof AgentAdapter && populationController.removeWithKilling(((AgentAdapter) agentAddress));
    }

    @Override
    public List<AgentAddress> queryLocalAgentsAddresses() {
        return populationController.getAllAdapters().stream().collect(toList());
    }

    @Override
    public AgentAddress queryAddress() {
        checkAgentContext();
        return currentAgentContext;
    }

    @Override
    public List<AgentAddress> queryOtherLocalAgents() {
        checkAgentContext();

        return populationController.getAllAdapters().stream()
            .filter(a -> a != currentAgentContext)
            .collect(toList());
    }

    @Override
    public List<AgentAddress> queryOtherLocalAgentsOfSameType() {
        checkAgentContext();
        return queryOtherLocalAgentsOfType(currentAgentContext.getAgent().getClass());
    }

    @Override
    public List<AgentAddress> queryOtherLocalAgentsOfType(Class<? extends Agent> agentClazz) throws UnsupportedAgentTypeException {
        checkAgentContext();
        creationCtxt.checkAgentClazz(agentClazz);
        return populationController.getAdaptersForAgentsOfType(agentClazz).stream()
            .filter(a -> a != currentAgentContext)
            .collect(toList());
    }

    @Override
    public void die() {
        checkAgentContext();
        populationController.scheduleRemoveWithKilling(currentAgentContext);
    }

    @Override
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }

    public void setControlAgentContext() {
        currentAgentContext = null;
    }

    public void setCurrentAgentContext(AgentAdapter currentAgentContext) {
        checkNotNull(currentAgentContext);
        this.currentAgentContext = currentAgentContext;
    }

    private void checkAgentContext() {
        checkState(currentAgentContext != null, "Current agent context is not set.");
    }

    private boolean isControllAgentContext() {
        return currentAgentContext == null;
    }


}
