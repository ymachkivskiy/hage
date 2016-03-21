package org.hage.platform.component.structure.grid;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.structure.definition.Position;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class ConnectionRepositoryHolder implements GridConnectionsRepository {

    private GridConnectionsRepository repository;

    @Override
    public boolean areNeighbors(Position first, Position second) {
        return repository.areNeighbors(first, second);
    }

    @Override
    public Neighbors getNeighbors(Position position) {
        return repository.getNeighbors(position);
    }

    void setRepository(GridConnectionsRepository wrappedRepository) {
        checkArgument(wrappedRepository != this, "Cannot wrap myself");
        log.debug("Set repository to '{}'", wrappedRepository);

        this.repository = wrappedRepository;
    }
}
