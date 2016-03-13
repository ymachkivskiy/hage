package org.hage.platform.config.load.generate.count;

import org.hage.platform.config.load.definition.agent.AgentCountData;

import static java.lang.Math.abs;

public class RandomCountProvider extends AbstractCountProvider {

    @Override
    protected Integer getCountInternal(AgentCountData countData) {
        return abs(rand.nextInt());
    }

}
