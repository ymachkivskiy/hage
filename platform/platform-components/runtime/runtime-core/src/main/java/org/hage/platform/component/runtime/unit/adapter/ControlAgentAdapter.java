package org.hage.platform.component.runtime.unit.adapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ControlAgentAdapter implements ControlContext {
    private final CommonContextAdapter context;
    private final ControlAgent controlAgent;

    public void performStep() {
        controlAgent.step(this);
    }

    @Override
    public Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        //todo : NOT IMPLEMENTED
        return Collections.emptySet();
    }

    @Override
    public <T extends Agent> List<T> queryAgentsOfType(Class<T> clazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED
        return Collections.emptyList();
    }

    @Override
    public <T extends Agent> T newAgent(Class<T> clazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public UnitAddress queryUnit() {
        return context.queryUnit();
    }

    @Override
    public Neighbors querySurroundingUnits() {
        return context.querySurroundingUnits();
    }

    @Override
    public void notifyStopConditionSatisfied() {
        context.notifyStopConditionSatisfied();
    }
}
