package org.hage.platform.component.runtime.activepopulation;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.runtime.unit.context.AgentExecutionContextEnvironment;
import org.hage.platform.component.runtime.unit.faces.AgentsRunner;
import org.hage.platform.component.runtime.util.StatefulFinisher;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@PrototypeComponent
public class UnitActivePopulationController implements AgentsRunner, AgentsTargetEnvironment, AgentsController {

    private final AgentExecutionContextEnvironment agentEnvironment;

    private final AtomicLong agentIdCounter = new AtomicLong();

    private final List<AgentAdapter> toBeRemoved = new LinkedList<>();
    private final List<AgentAdapter> toBeAdded = new LinkedList<>();
    private final List<Agent> disposedAgents = new LinkedList<>();

    private final Set<AgentAdapter> agentsAdapters = new HashSet<>();

    private Optional<ControlAgentAdapter> controlAgent = empty();

    private boolean removeAllAdapters = false;

    @Autowired
    private StatefulFinisher statefulFinisher;

    public UnitActivePopulationController(AgentExecutionContextEnvironment environment) {
        this.agentEnvironment = environment;
    }

    public void performPostProcessing() {
        flushAgents();
        finishDisposedAgents();
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
    public void setControlAgent(ControlAgent controlAgent) {
        log.debug("Set control agent to {}", controlAgent);
        this.controlAgent = of(new ControlAgentAdapter(agentEnvironment, controlAgent));
    }

    @Override
    public void addAgentsImmediately(Collection<? extends Agent> agents) {
        log.debug("Add agents into local population {}", agents);
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(agentsAdapters::add);
    }

    @Override
    public void scheduleAddAgents(Collection<? extends Agent> agents) {
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(toBeAdded::add);
    }

    public boolean remove(AgentAdapter agentAdapter) {
        log.debug("Remove agent {} immediately", agentAdapter.getFriendlyIdentifier());
        return agentsAdapters.remove(agentAdapter);
    }

    public boolean removeWithKilling(AgentAdapter agentAdapter) {
        boolean result = remove(agentAdapter);
        if (result) {
            disposedAgents.add(agentAdapter.getAgent());
        }
        return result;
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

    @Override
    public boolean isLocalAgentAdapter(AgentAdapter agentAdapter) {
        return agentAdapter != null && agentsAdapters.contains(agentAdapter);
    }

    private void flushAgents() {

        if (removeAllAdapters) {
            log.debug("Remove all existing agents {}", agentsAdapters);
            agentsAdapters.clear();
        } else {
            log.debug("Remove all killed and dead agents {}", toBeRemoved);
            agentsAdapters.removeAll(toBeRemoved);
        }

        log.debug("Append all created during step agents {}", toBeAdded);
        agentsAdapters.addAll(toBeAdded);

        toBeRemoved.clear();
        toBeAdded.clear();

        removeAllAdapters = false;
    }

    private void finishDisposedAgents() {
        log.debug("Finish {} disposed agents", disposedAgents.size());

        statefulFinisher.finish(disposedAgents);
        disposedAgents.clear();
    }

    private AgentAdapter createAdapterFor(Agent agent) {
        return new AgentAdapter(agent, agentIdCounter.getAndIncrement(), agentEnvironment);
    }

}
