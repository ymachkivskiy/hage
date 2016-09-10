package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.runtime.migration.internal.InternalMigrantsProvider;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.execution.phase.ExecutionPhaseType.PRE__INTERNAL_MIGRATION_PROCESSING;


@SingletonComponent
public class InternalMigrationProcessPhase implements ExecutionPhase {

    @Autowired
    private InternalMigrationTaskFactory taskFactory;
    @Autowired
    private InternalMigrantsProvider migrationGroupsProvider;

    @Override
    public ExecutionPhaseType getType() {
        return PRE__INTERNAL_MIGRATION_PROCESSING;
    }

    @Override
    public Collection<? extends Runnable> getTasks(long currentStep) {
        return migrationGroupsProvider.takeMigrationGroups().stream()
            .map(taskFactory::migrationTaskFor)
            .collect(toList());
    }

}
