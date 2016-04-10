package org.hage.platform.component.structure.connections;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.structure.StructureException;
import org.hage.platform.component.structure.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@HageComponent
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
