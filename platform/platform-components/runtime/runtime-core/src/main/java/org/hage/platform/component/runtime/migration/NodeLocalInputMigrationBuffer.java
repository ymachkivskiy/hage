package org.hage.platform.component.runtime.migration;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.*;

@Slf4j
@SingletonComponent
class NodeLocalInputMigrationBuffer implements InputMigrationQueue, LocalMigrationGroupsProvider {

    private static final int INITIAL_CAPACITY = 1000;

    private final ArrayList<Migrant> migrants = new ArrayList<>(INITIAL_CAPACITY);

    @Override
    public void acceptMigrant(Migrant migrant) {
        log.debug("Accept migrant {}", migrant);

        synchronized (migrants) {
            migrants.add(migrant);
        }
    }

    @Override
    public void acceptMigrants(Collection<Migrant> migrants) {
        log.debug("Accept migrants {}", migrants);

        synchronized (this.migrants) {
            this.migrants.addAll(migrants);
        }
    }

    @Override
    public List<LocalMigrationGroup> takeMigrationGroups() {
        log.debug("Take migrations groups");

        List<Migrant> tmpMigrants;

        synchronized (migrants) {
            tmpMigrants = new ArrayList<>(migrants);
            migrants.clear();
            migrants.ensureCapacity(INITIAL_CAPACITY);
        }

        return toMigrationGroups(tmpMigrants);
    }

    private List<LocalMigrationGroup> toMigrationGroups(List<Migrant> tmpMigrants) {
        return tmpMigrants.stream()
            .collect(groupingBy(
                Migrant::getMigrationTarget,
                mapping(Migrant::getMigrantAgent, toList())))
            .entrySet().stream()
            .map(e -> new LocalMigrationGroup(e.getKey(), e.getValue()))
            .collect(toList());
    }

}
