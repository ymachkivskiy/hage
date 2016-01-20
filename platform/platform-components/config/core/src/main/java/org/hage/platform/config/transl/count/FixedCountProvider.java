package org.hage.platform.config.transl.count;

import org.hage.platform.config.def.agent.AgentCountData;

class FixedCountProvider extends AbstractCountProvider {

    protected FixedCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return countData.getValue().get();
    }

}
