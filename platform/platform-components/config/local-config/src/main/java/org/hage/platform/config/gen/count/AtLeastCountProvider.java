package org.hage.platform.config.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

public class AtLeastCountProvider extends AbstractCountProvider {

    public AtLeastCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return Math.abs(rand.nextInt()) + countData.getValue().get();
    }

}
