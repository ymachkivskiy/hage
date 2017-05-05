package org.hage.platform.node.structure.connections;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.structure.StructureException;
import org.hage.platform.node.structure.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@SingletonComponent
@Slf4j
class StructureCreator {

    @Autowired(required = false)
    private List<StructureCreationStrategy> creationStrategies;

    public Structure createFor(StructureDefinition structure) {
        log.debug("Creating grid connections repository using structure definition '{}'", structure);

        for (val strategy : nullSafe(creationStrategies)) {
            log.debug("Checking strategy '{}'", strategy);

            if (strategy.isApplicableFor(structure)) {
                log.debug("Found matching strategy '{}'", strategy);

                return strategy.createUsingDefinition(structure);
            }
        }

        log.error("Could not find strategy for configuring grid connections using structure definition '{}'", structure);
        throw new StructureException("Could not find strategy for configuring grid connections");
    }

}
