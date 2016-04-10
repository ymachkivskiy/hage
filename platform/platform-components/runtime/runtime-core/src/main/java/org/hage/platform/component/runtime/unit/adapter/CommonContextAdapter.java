package org.hage.platform.component.runtime.unit.adapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;
import org.hage.platform.simulation.runtime.CommonContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@HageComponent
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CommonContextAdapter implements CommonContext {

    private final Position position;


    @Override
    public UnitAddress queryUnit() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public Neighbors querySurroundingUnits() {
        //todo : NOT IMPLEMENTED
        return null;
    }

    @Override
    public void notifyStopConditionSatisfied() {
        //todo : NOT IMPLEMENTED

    }
}
