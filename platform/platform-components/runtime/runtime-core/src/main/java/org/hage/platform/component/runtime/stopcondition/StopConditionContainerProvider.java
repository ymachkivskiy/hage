package org.hage.platform.component.runtime.stopcondition;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.container.StopConditionProvider;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@SingletonComponent
@Slf4j
class StopConditionContainerProvider implements StopConditionProvider {

    @Autowired
    private MutableInstanceContainer globalInstanceContainer;

    @Override
    public Optional<StopCondition> getStopCondition() {
        return ofNullable(globalInstanceContainer.getInstance(StopCondition.class));
    }

    void configure(Class<? extends StopCondition> stopConditionClazz) {
        Optional<Class<? extends StopCondition>> oStopCond = ofNullable(stopConditionClazz);
        log.debug("Registering stop condition class {}", oStopCond);
        oStopCond.ifPresent(globalInstanceContainer::addSingletonComponent);
    }


}
