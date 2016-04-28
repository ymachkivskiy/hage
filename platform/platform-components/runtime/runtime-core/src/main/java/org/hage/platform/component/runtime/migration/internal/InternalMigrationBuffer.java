package org.hage.platform.component.runtime.migration.internal;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.migration.InputMigrationQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.*;

@Slf4j
@SingletonComponent
class InternalMigrationBuffer implements InputMigrationQueue, InternalMigrationGroupsProvider {

    private static final int INITIAL_CAPACITY = 256;

    private final ArrayList<InternalMigrationGroup> migrationGroups = new ArrayList<>(INITIAL_CAPACITY);

    @Override
    public void acceptMigrants(Collection<InternalMigrationGroup> migrationGroups) {
        log.debug("Accept migration groups {}", migrationGroups);

        synchronized (this.migrationGroups) {
            this.migrationGroups.addAll(migrationGroups);
        }
    }

    @Override
    public List<InternalMigrationGroup> takeMigrationGroups() {
        log.debug("Take migrations groups");

        List<InternalMigrationGroup> tmpMigrationGroups;

        synchronized (migrationGroups) {
            tmpMigrationGroups = new ArrayList<>(migrationGroups);
            migrationGroups.clear();
            migrationGroups.ensureCapacity(INITIAL_CAPACITY);
        }

        List<InternalMigrationGroup> migrationGroups = mergeMigrationGroupsWithSameTargets(tmpMigrationGroups);

        log.debug("Got migration groups {}", migrationGroups);

        return migrationGroups;
    }

    private static List<InternalMigrationGroup> mergeMigrationGroupsWithSameTargets(List<InternalMigrationGroup> tmpMigrationGroups) {
        return tmpMigrationGroups.stream()
            .collect(groupingBy(
                InternalMigrationGroup::getTargetPosition,
                mapping(
                    InternalMigrationGroup::getMigrants,
                    collectingAndThen(toList(), a -> a.stream().flatMap(List::stream).collect(toList()))// TODO: check if there is a better way
                ))
            ).entrySet().stream()
            .map(e -> new InternalMigrationGroup(e.getKey(), e.getValue()))
            .collect(toList());
    }

}
