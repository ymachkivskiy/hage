package org.hage.platform.component.simulationconfig.load.generate.count;

import org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData;

public class FixedCountProvider extends AbstractCountProvider {

    public FixedCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return countData.getValue().get();
    }

}
