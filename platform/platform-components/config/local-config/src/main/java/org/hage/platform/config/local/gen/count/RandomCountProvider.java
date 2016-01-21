package org.hage.platform.config.local.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

class RandomCountProvider extends AbstractCountProvider {

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return Math.abs(rand.nextInt());
    }

}
