package org.hage.platform.node.runtime.activepopulation;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.node.execution.monitor.AgentsInfo;
import org.hage.platform.node.runtime.container.dependency.DependenciesEraser;
import org.hage.platform.node.runtime.unit.AgentExecutionContextEnvironment;
import org.hage.platform.node.runtime.unit.AgentsRunner;
import org.hage.platform.node.runtime.unit.UnitComponent;
import org.hage.platform.node.runtime.unit.UnitContainer;
import org.hage.platform.node.runtime.util.StatefulFinisher;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;

@Slf4j
@PrototypeComponent
public class UnitActivePopulationController implements AgentsRunner, AgentsTargetEnvironment, AgentsController, UnitComponent {

    private final AtomicLong agentIdCounter = new AtomicLong();

    private final AgentExecutionContextEnvironment agentEnvironment;

    private Optional<PopulationControllerInitialState> initialState;

    private final List<AgentAdapter> toBeRemoved = new LinkedList<>();
    private final List<AgentAdapter> toBeAdded = new LinkedList<>();
    private final List<Agent> disposedAgents = new LinkedList<>();

    private final Set<AgentAdapter> agentsAdapters = new HashSet<>();

    private Optional<ControlAgentAdapter> controlAgent = empty();

    private boolean removeAllAdapters = false;

    private AgentsInfo agentsInfo = new AgentsInfo(0, 0, 0);

    @Setter
    private UnitContainer unitContainer;

    @Autowired
    private StatefulFinisher statefulFinisher;
    @Autowired
    private DependenciesEraser dependenciesEraser;

    public UnitActivePopulationController(AgentExecutionContextEnvironment agentExecutionEnv) {
        this(agentExecutionEnv, null);
    }

    public UnitActivePopulationController(AgentExecutionContextEnvironment agentExecutionEnv, PopulationControllerInitialState initialState) {
        this.agentEnvironment = agentExecutionEnv;
        this.initialState = ofNullable(initialState);
    }


    //region actions

    public void performPostProcessing() {
        calculateAgentsInfo();
        removeQueriedAgents();
        finishDisposedAgents();
        addQueriedAgents();
    }

    @Override
    public void runControlAgent() {
        log.debug("Run control agent");
        controlAgent.ifPresent(ControlAgentAdapter::performStep);
    }

    @Override
    public void runAgents() {
        log.debug("Run all agents");
        agentsAdapters.forEach(AgentAdapter::performStep);
    }

    @Override
    public AgentsInfo getInfo() {
        return agentsInfo;
    }

    //endregion


    public void setControlAgent(ControlAgent controlAgent) {
        log.debug("Set control agent to {}", controlAgent);
        this.controlAgent = of(new ControlAgentAdapter(agentEnvironment, controlAgent));
    }

    @Override
    public void performPostConstruction() {
        applyInitialState(initialState.orElse(createInitialState()));
        initialState = empty();
    }

    private PopulationControllerInitialState createInitialState() {
        return PopulationControllerInitialState.initialStateWithControlAgent(unitContainer.getControlAgentInstance().orElse(null));
    }

    private void applyInitialState(PopulationControllerInitialState state) {
        state.getControlAgent().ifPresent(
            ca -> {
                unitContainer.injectDependencies(ca);
                setControlAgent(ca);
            }
        );
        addAgentsImmediately(state.getAgents());
    }

    //region add/remove agents

    @Override
    public void addAgentsImmediately(Collection<? extends Agent> agents) {
        log.debug("Add agents into local population {}", agents);
        agents.stream()
            .peek(unitContainer::injectDependencies)
            .map(this::createAdapterFor)
            .forEach(agentsAdapters::add);
    }

    @Override
    public void scheduleAddAgents(Collection<? extends Agent> agents) {
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(toBeAdded::add);
    }

    public void scheduleRemove(Collection<AgentAdapter> agentsAdapters) {
        log.debug("Insert agents to be removed {}", agentsAdapters);
        toBeRemoved.addAll(agentsAdapters);
    }

    @Override
    public void scheduleRemoveAll() {
        log.debug("Mark all existing agents to be removed");
        removeAllAdapters = true;
    }

    @Override
    public void scheduleRemoveWithKilling(AgentAdapter agentAdapter) {
        log.debug("Remove agent {} with killing", agentAdapter);

        toBeRemoved.add(agentAdapter);
        disposedAgents.add(agentAdapter.getAgent());
    }

    //endregion

    @Override
    public <T extends Agent> List<AgentAdapter> getAdaptersForAgentsOfType(Class<T> agentClazz) {
        return agentsAdapters.stream()
            .filter(agentAdapter -> agentAdapter.getAgent().getClass().equals(agentClazz))
            .collect(toList());
    }

    @Override
    public Collection<AgentAdapter> getAllAdapters() {
        return unmodifiableSet(agentsAdapters);
    }

    public List<Agent> serializeAgents() {
        log.debug("Serializing agents");

        clearDependencies(agentsAdapters);

        return agentsAdapters
            .stream()
            .map(AgentAdapter::getAgent)
            .collect(toList());
    }

    public Optional<ControlAgent> serializeControlAgent() {
        log.debug("Serializing control agent");

        Optional<ControlAgent> controlAgent = this.controlAgent.map(ControlAgentAdapter::getControlAgent);
        controlAgent.ifPresent(dependenciesEraser::eraseDependencies);

        return controlAgent;
    }

    @Override
    public boolean isLocalAgentAdapter(AgentAdapter agentAdapter) {
        return agentAdapter != null && agentsAdapters.contains(agentAdapter);
    }

    private void calculateAgentsInfo() {
        agentsInfo = new AgentsInfo(
            agentsAdapters.size(),
            toBeAdded.size(),
            removeAllAdapters ? agentsAdapters.size() : toBeRemoved.size()
        );
    }

    private void removeQueriedAgents() {

        if (removeAllAdapters) {
            log.debug("Remove all existing agents {}", agentsAdapters);
            toBeRemoved.addAll(agentsAdapters);
            agentsAdapters.clear();
        } else {
            log.debug("Remove all killed and dead agents {}", toBeRemoved);
            agentsAdapters.removeAll(toBeRemoved);
        }

        clearDependencies(toBeRemoved);
        toBeRemoved.clear();
        removeAllAdapters = false;
    }

    private void addQueriedAgents() {
        log.debug("Append all created during step agents {}", toBeAdded);
        agentsAdapters.addAll(toBeAdded);
        insertDependencies(toBeAdded);
        toBeAdded.clear();
    }

    private void finishDisposedAgents() {
        log.debug("Finish {} disposed agents", disposedAgents.size());

        statefulFinisher.finish(disposedAgents);
        disposedAgents.clear();
    }

    private AgentAdapter createAdapterFor(Agent agent) {
        return new AgentAdapter(agent, agentIdCounter.getAndIncrement(), agentEnvironment);
    }

    private void insertDependencies(List<AgentAdapter> toBeAdded) {
        log.debug("Insert dependencies for all agents {}", toBeAdded);
        toBeAdded
            .stream()
            .map(AgentAdapter::getAgent)
            .forEach(unitContainer::injectDependencies);
    }

    private void clearDependencies(Collection<AgentAdapter> toBeRemoved) {
        log.debug("Erase dependencies from all agents {}", toBeRemoved);
        toBeRemoved
            .stream()
            .map(AgentAdapter::getAgent)
            .forEach(dependenciesEraser::eraseDependencies);
    }

}
