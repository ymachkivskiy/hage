package org.hage.platform.node.runtime.stopcondition;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.simulationconfig.CoreConfigurerAdapter;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StopConditionCoreConfigurer extends CoreConfigurerAdapter<Class<? extends StopCondition>> {

    @Autowired
    private StopConditionContainerProvider stopConditionContainerSupplier;

    public StopConditionCoreConfigurer(int order) {
        super(config -> config.getCommon().getStopCondition(), order);
    }

    @Override
    protected void configureWithNarrow(Class<? extends StopCondition> stopCondition) {
        log.debug("Configure stop condition.");
        stopConditionContainerSupplier.configure(stopCondition);
    }

}
