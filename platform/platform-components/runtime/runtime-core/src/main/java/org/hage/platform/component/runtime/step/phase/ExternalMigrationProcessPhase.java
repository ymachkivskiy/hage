package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationGroupsProvider;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.execution.phase.ExecutionPhaseType.POST__EXTERNAL_MIGRATION_PROCESSING;

@SingletonComponent
public class ExternalMigrationProcessPhase implements ExecutionPhase {

    @Autowired
    private ExternalMigrationTaskFactory taskFactory;
    @Autowired
    private ExternalMigrationGroupsProvider migrationGroupsProvider;

    @Override
    public ExecutionPhaseType getType() {
        return POST__EXTERNAL_MIGRATION_PROCESSING;
    }

    @Override
    public Collection<? extends Runnable> getTasks(long currentStep) {
        return migrationGroupsProvider.takeExternalMigrationGroups().stream()
            .map(taskFactory::taskForGroup)
            .collect(toList());
    }
}
