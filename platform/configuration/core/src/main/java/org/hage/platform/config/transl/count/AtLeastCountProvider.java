package org.hage.platform.config.transl.count;

import org.hage.platform.config.def.agent.AgentCountData;

class AtLeastCountProvider extends AbstractCountProvider {

    protected AtLeastCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return rand.nextInt() + countData.getValue().get();
    }

}
