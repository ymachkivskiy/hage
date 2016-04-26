package org.hage.platform.component.runtime.migration;

import java.util.Collection;

public interface InputMigrationQueue {
    void acceptMigrant(Migrant migrant);

    void acceptMigrants(Collection<Migrant> migrants);
}
