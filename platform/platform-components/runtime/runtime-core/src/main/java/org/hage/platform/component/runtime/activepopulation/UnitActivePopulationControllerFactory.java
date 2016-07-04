package org.hage.platform.component.runtime.activepopulation;

import org.hage.platform.component.runtime.unit.AgentExecutionContextEnvironment;

public interface UnitActivePopulationControllerFactory {
    UnitActivePopulationController createActivePopulationControllerForExecutionEnvironment(AgentExecutionContextEnvironment agentEnvironment);
}
