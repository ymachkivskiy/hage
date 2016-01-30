package org.hage.platform.config.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

public class AtMostCountProvider extends AbstractCountProvider {

    public AtMostCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return rand.nextInt(countData.getValue().get() - 1) + 1;
    }

}
