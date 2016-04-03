package org.hage.platform.component.runtime.unit.agent;

import lombok.RequiredArgsConstructor;
import org.hage.platform.simulation.runtime.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ControlAgentAdapter implements ControlContext {
    private final ControlAgent controlAgent;

    public void performStep() {
        controlAgent.step(this);
    }

    @Override
    public Set<Class<? extends Agent>> getSupportedAgentsTypes() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public <T extends Agent> List<T> queryAgentsOfType(Class<T> clazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public <T extends Agent> T newAgent(Class<T> clazz) throws UnsupportedAgentTypeException {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public UnitAddress queryUnit() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public Set<UnitAddress> querySurroundingUnits() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
