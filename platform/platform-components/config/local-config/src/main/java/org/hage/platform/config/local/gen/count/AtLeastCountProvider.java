package org.hage.platform.config.local.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

class AtLeastCountProvider extends AbstractCountProvider {

    protected AtLeastCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return Math.abs(rand.nextInt()) + countData.getValue().get();
    }

}
