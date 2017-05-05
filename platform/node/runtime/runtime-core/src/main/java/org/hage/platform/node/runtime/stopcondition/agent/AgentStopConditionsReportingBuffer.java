package org.hage.platform.node.runtime.stopcondition.agent;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.runtime.stopcondition.StopConditionChecker;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.synchronizedCollection;

@SingletonComponent
@Slf4j
@Order(1)
class AgentStopConditionsReportingBuffer implements StopConditionChecker, AgentStopConditionReporter {

    private final Collection<AgentReport> reports = synchronizedCollection(new ArrayList<>());

    @Override
    public void reportStopConditionReached(AgentReport agentReport) {
        log.debug("Got stop condition satisfied report {}", agentReport);
        reports.add(agentReport);
    }

    @Override
    public boolean isSatisfied() {
        boolean isSatisfied = checkIfSatisfied();

        log.info("Agent based stop condition is satisfied {}", isSatisfied);

        return isSatisfied;
    }

    private boolean checkIfSatisfied() {

        for (AgentReport report : reports) {
            log.info("Got satisfied agent stop condition raised by {}", report);
        }

        return !reports.isEmpty();
    }

}
