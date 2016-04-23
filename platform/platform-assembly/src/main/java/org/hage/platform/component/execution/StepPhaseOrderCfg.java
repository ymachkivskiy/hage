package org.hage.platform.component.execution;

import org.hage.platform.component.execution.step.StepPhaseFactory;
import org.hage.platform.component.runtime.stepphase.AgentsStepPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hage.platform.component.execution.step.StaticFactoryBuilder.staticFactoryBuilder;

@Configuration
class StepPhaseOrderCfg {

    @Autowired
    private AgentsStepPhase agentsStepPhase;

    @Bean
    public StepPhaseFactory stepPhaseFactory() {
        return staticFactoryBuilder()
            .addNextIndependentPhases(agentsStepPhase)
            .build();
    }

}
