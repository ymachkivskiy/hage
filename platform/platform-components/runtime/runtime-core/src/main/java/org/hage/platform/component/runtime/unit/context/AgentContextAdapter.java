package org.hage.platform.component.runtime.unit.context;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.execution.monitor.ExecutionMonitor;
import org.hage.platform.component.runtime.activepopulation.AgentAdapter;
import org.hage.platform.component.runtime.activepopulation.AgentsController;
import org.hage.platform.component.runtime.container.AgentsCreator;
import org.hage.platform.component.runtime.location.AgentsUnitAddress;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.migration.OutputMigrationQueue;
import org.hage.platform.component.runtime.stopcondition.agent.AgentStopConditionReporter;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentAddress;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.context.AgentInitializer;
import org.hage.platform.simulation.runtime.context.UnsupportedAgentTypeException;
import org.hage.platform.simulation.runtime.control.AddressedAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.runtime.stopcondition.agent.AgentReport.reporterForAgentIn;
import static org.hage.platform.component.runtime.stopcondition.agent.AgentReport.reporterForControlAgentOf;
import static org.hage.util.CollectionUtils.nullSafe;

@Slf4j
@PrototypeComponent
public class AgentContextAdapter implements AgentManageContext, ControlAgentManageContext {

    private static final AgentInitializer<?> EMPTY_INITIALIZER = agent -> {
    };

    private final UnitLocationController locationController;
    private final AgentsCreator agentsCreator;
    private final AgentsController agentsController;

    private AgentAdapter currentAgentContext;

    @Autowired
    private OutputMigrationQueue outputMigrationQueue;
    @Autowired
    private ExecutionMonitor executionMonitor;
    @Autowired
    private AgentStopConditionReporter stopConditionReporter;

    public AgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController) {
        this.locationController = locationController;
        this.agentsCreator = agentsCreator;
        this.agentsController = agentsController;
    }

    @Override
    public final Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        return agentsCreator.getSupportedAgentsTypes();
    }

    @Override
    public final <T extends Agent> void newAgent(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        agentsCreator.newAgents(agentClazz, (AgentInitializer<T>) EMPTY_INITIALIZER, 1, isControlAgentContext());
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, int agentsNumber) throws UnsupportedAgentTypeException {
        agentsCreator.newAgents(agentClazz, (AgentInitializer<T>) EMPTY_INITIALIZER, agentsNumber, isControlAgentContext());
    }

    @Override
    public final <T extends Agent> void newAgent(Class<T> agentClazz, AgentInitializer<T> initializer) throws UnsupportedAgentTypeException {
        agentsCreator.newAgents(agentClazz, initializer, 1, isControlAgentContext());
    }

    @Override
    public <T extends Agent> void newAgents(Class<T> agentClazz, AgentInitializer<T> initializer, int agentsNumber) throws UnsupportedAgentTypeException {
        agentsCreator.newAgents(agentClazz, initializer, agentsNumber, isControlAgentContext());
    }

    @Override
    public final UnitAddress queryLocalUnit() {
        return locationController.queryLocalUnit();
    }

    @Override
    public final Neighbors querySurroundingUnits() {
        return locationController.querySurroundingUnits();
    }

    @Override
    public <T extends Agent> List<AddressedAgent<T>> queryAgentsOfType(Class<T> agentClazz) throws UnsupportedAgentTypeException {
        agentsCreator.checkAgentClazz(agentClazz);
        return agentsController.getAdaptersForAgentsOfType(agentClazz).stream()
            .map(agentAdapter -> new AddressedAgent<>((T) agentAdapter.getAgent(), agentAdapter))
            .collect(toList());
    }

    @Override
    public void killAgent(AgentAddress agentAddress) {
        if (agentAddress instanceof AgentAdapter) {
            agentsController.scheduleRemoveWithKilling(((AgentAdapter) agentAddress));
        }
    }

    @Override
    public boolean migrateAllAgentsTo(UnitAddress targetUnitAddress) {
        if (!(targetUnitAddress instanceof AgentsUnitAddress)) {
            return false;
        }

        if (isControlAgentContext()) {
            AgentsUnitAddress targetUnit = (AgentsUnitAddress) targetUnitAddress;

            if (!locationController.isLegalMigrationTargetAddress(targetUnit)) {
                log.debug("Control agent of {} tries to send all agents to illegal target unit address {}", locationController.getPosition(), targetUnitAddress);
                return false;
            }

            List<Agent> migrants = agentsController.getAllAdapters().stream()
                .map(AgentAdapter::getAgent)
                .collect(toList());

            agentsController.scheduleRemoveAll();

            outputMigrationQueue.registerAgentsForMigration(migrants, targetUnit);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean migrateAgentsTo(Collection<AgentAddress> agentAddresses, UnitAddress targetUnitAddress) {
        if (!(targetUnitAddress instanceof AgentsUnitAddress)) {
            return false;
        }

        AgentsUnitAddress targetUnit = (AgentsUnitAddress) targetUnitAddress;

        List<AgentAdapter> agentAdapters = nullSafe(agentAddresses).stream()
            .filter(agentAddress -> agentAddress instanceof AgentAdapter)
            .map(agentAddress -> ((AgentAdapter) agentAddress))
            .filter(agentsController::isLocalAgentAdapter)
            .collect(toList());

        if (agentAdapters.isEmpty()) {
            return false;
        }

        if (!locationController.isLegalMigrationTargetAddress(targetUnit)) {
            log.debug("Agents {} tried to migrate to illegal target {}", agentAdapters, targetUnitAddress);
            return false;
        }

        agentsController.scheduleRemove(agentAdapters);

        List<Agent> migrants = agentAdapters.stream()
            .map(AgentAdapter::getAgent)
            .collect(toList());

        outputMigrationQueue.registerAgentsForMigration(migrants, targetUnit);

        return true;
    }

    @Override
    public boolean migrateAgentTo(AgentAddress agentAddress, UnitAddress targetUnitAddress) {
        return migrateAgentsTo(singletonList(agentAddress), targetUnitAddress);
    }

    @Override
    public boolean migrateTo(UnitAddress targetUnitAddress) {
        checkAgentContext();
        return migrateAgentTo(currentAgentContext, targetUnitAddress);
    }

    @Override
    public List<AgentAddress> queryLocalAgentsAddresses() {
        return agentsController.getAllAdapters().stream().collect(toList());
    }

    @Override
    public AgentAddress queryAddress() {
        checkAgentContext();
        return currentAgentContext;
    }

    @Override
    public List<AgentAddress> queryOtherLocalAgents() {
        checkAgentContext();

        return agentsController.getAllAdapters().stream()
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
        agentsCreator.checkAgentClazz(agentClazz);
        return agentsController.getAdaptersForAgentsOfType(agentClazz).stream()
            .filter(a -> a != currentAgentContext)
            .collect(toList());
    }

    @Override
    public void die() {
        checkAgentContext();
        agentsController.scheduleRemoveWithKilling(currentAgentContext);
    }

    @Override
    public long getCurrentStep() {
        return executionMonitor.getCurrentStepNumber();
    }

    @Override
    public void notifyStopConditionSatisfied() {
        if (isControlAgentContext()) {
            stopConditionReporter.reportStopConditionReached(reporterForControlAgentOf(locationController.getPosition()));
        } else {
            checkAgentContext();
            stopConditionReporter.reportStopConditionReached(reporterForAgentIn(locationController.getPosition(), currentAgentContext.getAgent()));
        }
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

    private boolean isControlAgentContext() {
        return currentAgentContext == null;
    }


}
