package org.hage.platform.node.runtime.activepopulation;

import org.hage.platform.node.runtime.unit.AgentExecutionContextEnvironment;

public interface UnitActivePopulationControllerFactory {
    UnitActivePopulationController createControllerWithExecutionEnvironment(AgentExecutionContextEnvironment agentEnvironment);

    UnitActivePopulationController createControllerWithExecutionEnvironmentAndInitialState(AgentExecutionContextEnvironment execEnv, PopulationControllerInitialState initialState);
}