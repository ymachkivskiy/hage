package org.hage.platform.node.runtime.migration.internal;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.runtime.migration.InputMigrationQueue;
import org.hage.platform.node.runtime.migration.InternalMigrantsInformationProvider;
import org.hage.platform.node.structure.Position;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

@Slf4j
@SingletonComponent
class InternalMigrationBuffer implements InputMigrationQueue, InternalMigrantsProvider, InternalMigrantsInformationProvider {

    private final List<InternalMigrationGroup> migrationGroups = new LinkedList<>();

    @Override
    public synchronized void acceptMigrants(Collection<InternalMigrationGroup> migrationGroups) {
        log.debug("Accept migration groups {}", migrationGroups);

        this.migrationGroups.addAll(migrationGroups);
    }

    @Override
    public synchronized List<InternalMigrationGroup> takeMigrationGroups() {
        log.debug("Take migrations groups");

        compactGroups();

        List<InternalMigrationGroup> tmpMigrationGroups = new ArrayList<>(migrationGroups);
        migrationGroups.clear();

        log.debug("Got migration groups {}", tmpMigrationGroups);

        return tmpMigrationGroups;
    }

    @Override
    public synchronized List<InternalMigrationGroup> takeMigrantsTo(Position position) {
        log.debug("Take all migrants to {}", position);

        compactGroups();

        return takeMatching(group -> group.getTargetPosition().equals(position));
    }

    @Override
    public synchronized int getNumberOfMigrantsTo(Position position) {

        int targetMigrantsNumber = migrationGroups.stream()
            .filter(group -> group.getTargetPosition().equals(position))
            .mapToInt(group -> group.getMigrants().size())
            .sum();

        log.debug("Number of migrants to {} is {}", position, targetMigrantsNumber);

        return targetMigrantsNumber;
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
