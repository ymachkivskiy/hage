package org.hage.platform.config.local.gen.count;

import org.hage.platform.config.def.agent.AgentCountData;

import static java.lang.Math.abs;

public class RandomCountProvider extends AbstractCountProvider {

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return abs(rand.nextInt());
    }

}
