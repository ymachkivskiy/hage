package org.hage.platform.component.execution;

import org.hage.platform.component.execution.action.ordering.ActionComparator;
import org.hage.platform.component.execution.action.ordering.DefaultActionComparator;
import org.hage.platform.component.execution.workplace.FixedStepCountStopCondition;
import org.hage.platform.component.execution.workplace.manager.DefaultWorkplaceManager;
import org.hage.platform.component.execution.workplace.manager.WorkplaceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformRuntimeCoreModuleConfiguration {

    @Bean
    public WorkplaceManager workplaceManager() {
        return new DefaultWorkplaceManager();
    }

    @Bean
    public FixedStepCountStopCondition stopCondition() {
        return new FixedStepCountStopCondition();
    }

    @Bean
    public ActionComparator actionComparator() {
        return new DefaultActionComparator();
    }

}
