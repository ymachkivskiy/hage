package org.hage.platform.config.load.generate.count;

import org.hage.platform.config.load.definition.agent.AgentCountData;

public class FixedCountProvider extends AbstractCountProvider {

    public FixedCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return countData.getValue().get();
    }

}
