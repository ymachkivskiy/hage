package org.hage.platform.component.runtime.stopcondition;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

@SingletonComponent
@Slf4j
public class StopConditionsChain {

    @Autowired(required = false)
    private List<StopConditionChecker> stopConditionCheckers = emptyList();

    public boolean stopConditionSatisfied() {
        log.debug("Check whether if one of stop conditions was satisfied");
        return stopConditionCheckers.stream()
            .filter(StopConditionChecker::isSatisfied)
            .findFirst()
            .isPresent();
    }

}
