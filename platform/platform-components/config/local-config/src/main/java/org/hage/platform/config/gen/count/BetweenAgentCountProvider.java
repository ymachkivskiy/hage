package org.hage.platform.config.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

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
