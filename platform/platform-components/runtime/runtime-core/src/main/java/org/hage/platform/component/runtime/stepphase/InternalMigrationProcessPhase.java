package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.StepPhase;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroupsProvider;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static java.util.stream.Collectors.toList;


@SingletonComponent
public class InternalMigrationProcessPhase implements StepPhase {

    @Autowired
    private InternalMigrationTaskFactory internalMigrationTaskFactory;
    @Autowired
    private InternalMigrationGroupsProvider internalMigrationGroupsProvider;

    @Override
    public String getPhaseName() {
        return "Internal migrations process";
    }

    @Override
    public Collection<? extends Runnable> getRunnable(long currentStep) {
        return internalMigrationGroupsProvider.takeMigrationGroups().stream()
            .map(internalMigrationTaskFactory::migrationTaskFor)
            .collect(toList());
    }

}
