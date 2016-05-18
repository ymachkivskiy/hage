package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.phase.StepPhase;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationGroupsProvider;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@SingletonComponent
public class ExternalMigrationProcessPhase implements StepPhase {

    @Autowired
    private ExternalMigrationTaskFactory taskFactory;
    @Autowired
    private ExternalMigrationGroupsProvider migrationGroupsProvider;

    @Override
    public String getPhaseName() {
        return "External migration processing";
    }

    @Override
    public Collection<? extends Runnable> getRunnable(long currentStep) {
        return migrationGroupsProvider.takeExternalMigrationGroups().stream()
            .map(taskFactory::taskForGroup)
            .collect(toList());
    }
}
