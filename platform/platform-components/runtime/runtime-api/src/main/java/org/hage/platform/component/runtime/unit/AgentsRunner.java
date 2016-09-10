package org.hage.platform.component.runtime.unit;

import org.hage.platform.component.execution.monitor.AgentsInfo;

public interface AgentsRunner {
    void runAgents();

    void runControlAgent();

    AgentsInfo getInfo();
}
