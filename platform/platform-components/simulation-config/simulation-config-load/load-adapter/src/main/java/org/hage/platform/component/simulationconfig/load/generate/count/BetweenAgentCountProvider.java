package org.hage.platform.component.simulationconfig.load.generate.count;

import org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData;

public class BetweenAgentCountProvider extends AbstractCountProvider {

    public BetweenAgentCountProvider() {
        super(ValueType.PRIMARY, ValueType.SECONDARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        int width = countData.getSecondaryValue().get() - countData.getValue().get();
        return rand.nextInt(width + 1) + countData.getValue().get();
    }

}
