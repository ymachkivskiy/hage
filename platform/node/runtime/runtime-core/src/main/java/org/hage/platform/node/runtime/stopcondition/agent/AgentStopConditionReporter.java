package org.hage.platform.node.runtime.stopcondition.agent;

public interface AgentStopConditionReporter {
    void reportStopConditionReached(AgentReport agentReport);
}
