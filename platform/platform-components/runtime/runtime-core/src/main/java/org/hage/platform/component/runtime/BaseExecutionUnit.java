package org.hage.platform.component.runtime;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.container.InstanceContainer;
import org.hage.platform.component.runtime.agent.AgentAdapter;
import org.hage.platform.component.runtime.agent.ControlAgentAdapter;
import org.hage.platform.component.runtime.unit.ExecutionUnit;
import org.hage.platform.component.runtime.unit.ExecutionUnitAddress;
import org.hage.platform.component.structure.definition.Position;
import org.hage.platform.util.connection.NodeAddress;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public class BaseExecutionUnit implements ExecutionUnit {

    private final Position position;
    private final Set<AgentAdapter> agentAdapters = new HashSet<>();
    private final AtomicReference<ControlAgentAdapter> controlAgent = new AtomicReference<>();

    private transient ExecutionUnitAddress currentAddress;
    @Setter
    private transient InstanceContainer instanceContainer;


    @Override
    public ExecutionUnitAddress getAddress() {
        return currentAddress;
    }

    @Override
    public void performControlAgentStep() {
        // TODO: to be implemented
        //0. if control agent present
        //1. create control agent context
        //2. perform step on control agent using created context
    }

    @Override
    public void performAgentsStep() {
// TODO: todo
        // for each agentadapter
        agentAdapters.forEach(AgentAdapter::performStep);
    }

    public void registerAgentAdapters(Collection<AgentAdapter> agentAdapters) {
        for (AgentAdapter agentsAdapter : agentAdapters) {
            registerAgentAdapter(agentsAdapter);
        }
    }

    public void registerAgentAdapter(AgentAdapter agentAdapter) {
        log.debug("Register agent adapter '{}' in unit '{}'", agentAdapter, position);

        agentAdapters.add(agentAdapter);
    }

    public void registerControlAgentAdapter(ControlAgentAdapter controlAgent) {
        log.debug("Register control agent '{}' for unit '{}'", controlAgent, position);

        if (!this.controlAgent.compareAndSet(null, controlAgent)) {
            log.warn("Trying to set control agent '{}' for unit '{}' while it has already defined control agent '{}'", controlAgent, position, this.controlAgent.get());
        }
    }

    public void updateNodeAddress(NodeAddress nodeAddress) {
        log.debug("Update current address of '{}' on node '{}'", position, nodeAddress);

        currentAddress = new ExecutionUnitAddress(nodeAddress, position);
    }

}
