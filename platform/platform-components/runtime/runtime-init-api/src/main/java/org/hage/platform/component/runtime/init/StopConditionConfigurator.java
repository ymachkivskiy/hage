package org.hage.platform.component.runtime.init;

import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

public interface StopConditionConfigurator {
    void configureWith(Class<? extends StopCondition> stopConditionClazz);
}
