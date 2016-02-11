package org.hage.platform.config.load.adapter.generate.count;

import org.hage.platform.config.load.definition.agent.AgentCountData;

public class AtLeastCountProvider extends AbstractCountProvider {

    public AtLeastCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return Math.abs(rand.nextInt()) + countData.getValue().get();
    }

}
