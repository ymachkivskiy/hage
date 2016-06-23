package org.hage.platform.component.runtime.migration.internal;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.migration.InputMigrationQueue;
import org.hage.platform.component.structure.Position;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

@Slf4j
@SingletonComponent
class InternalMigrationBuffer implements InputMigrationQueue, InternalMigrantsProvider {

    private final List<InternalMigrationGroup> migrationGroups = new LinkedList<>();

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
            compactGroups();
            tmpMigrationGroups = new ArrayList<>(migrationGroups);
            migrationGroups.clear();
        }

        log.debug("Got migration groups {}", tmpMigrationGroups);

        return tmpMigrationGroups;
    }

    @Override
    public List<InternalMigrationGroup> takeMigrantsTo(Position position) {
        log.debug("Take all migrants to {}", position);

        List<InternalMigrationGroup> positionGroups = new ArrayList<>();

        synchronized (migrationGroups) {
            compactGroups();
            positionGroups.addAll(takeMatching(group -> group.getTargetPosition().equals(position)));
        }

        return positionGroups;
    }

    private List<InternalMigrationGroup> takeMatching(Predicate<InternalMigrationGroup> matchingPredicate) {
        List<InternalMigrationGroup> matching = new ArrayList<>();

        for (Iterator<InternalMigrationGroup> it = migrationGroups.iterator(); it.hasNext(); ) {
            InternalMigrationGroup group = it.next();
            if (matchingPredicate.test(group)) {
                it.remove();
                matching.add(group);
            }
        }

        return matching;
    }

    private void compactGroups() {
        ArrayList<InternalMigrationGroup> tmpMigrationGroups = new ArrayList<>(migrationGroups);

        migrationGroups.clear();

        tmpMigrationGroups.stream()
            .collect(groupingBy(
                InternalMigrationGroup::getTargetPosition,
                mapping(
                    InternalMigrationGroup::getMigrants,
                    collectingAndThen(toList(), a -> a.stream().flatMap(List::stream).collect(toList()))// TODO: check if there is a better way
                ))
            ).entrySet().stream()
            .map(e -> new InternalMigrationGroup(e.getKey(), e.getValue()))
            .collect(toCollection(() -> migrationGroups));
    }

}
