package org.hage.platform.component.simulationconfig.load.generate.count;

import org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData;

public class AtLeastCountProvider extends AbstractCountProvider {

    public AtLeastCountProvider() {
        super(ValueType.PRIMARY);
    }

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return Math.abs(rand.nextInt()) + countData.getValue().get();
    }

}
