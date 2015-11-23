package org.hage.platform.config.transl.count;

import org.hage.platform.config.def.agent.AgentCountData;

class RandomCountProvider extends AbstractCountProvider {

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return Math.abs(rand.nextInt()) + 1;
    }

}
