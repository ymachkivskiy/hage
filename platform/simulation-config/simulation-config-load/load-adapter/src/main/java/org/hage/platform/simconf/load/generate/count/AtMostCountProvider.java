package org.hage.platform.simconf.load.generate.count;

import org.hage.platform.simconf.load.definition.agent.AgentCountData;

public class AtMostCountProvider extends AbstractCountProvider {

    public AtMostCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return rand.nextInt(countData.getValue().get() - 1) + 1;
    }

}
