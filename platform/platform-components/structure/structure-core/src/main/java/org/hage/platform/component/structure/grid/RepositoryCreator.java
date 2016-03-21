package org.hage.platform.component.structure.grid;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.structure.StructureException;
import org.hage.platform.component.structure.definition.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;


@Slf4j
public class RepositoryCreator implements GridConnectionsConfigurator {

    @Autowired
    private ConnectionRepositoryHolder repositoryWrapper;
    @Autowired(required = false)
    private List<RepositoryCreationStrategy> creationStrategies;


    @Override
    public void configure(StructureDefinition structure) {
        log.debug("Creating grid connections repository using structure definition '{}'", structure);

        for (RepositoryCreationStrategy strategy : nullSafe(creationStrategies)) {
            log.debug("Checking strategy '{}'", strategy);

            if (strategy.isApplicableFor(structure)) {
                log.debug("Found matching strategy '{}'", strategy);

                repositoryWrapper.setRepository(strategy.create(structure));
                return;
            }
        }

        log.error("Could not find strategy for configuring grid connections using structure definition '{}'", structure);
        throw new StructureException("Could not find strategy for configuring grid connections");
    }
}
