package org.hage.platform.node.runtime.unit;

import org.hage.platform.node.execution.monitor.AgentsInfo;

public interface AgentsRunner {
    void runAgents();

    void runControlAgent();

    AgentsInfo getInfo();
}
