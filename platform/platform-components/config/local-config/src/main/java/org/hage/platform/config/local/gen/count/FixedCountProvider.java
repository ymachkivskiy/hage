package org.hage.platform.config.local.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

public class FixedCountProvider extends AbstractCountProvider {

    public FixedCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return countData.getValue().get();
    }

}
