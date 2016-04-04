package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.unit.adapter.CommonContextAdapter;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class AgentsUnitFactory {


    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private MutableInstanceContainer mutableInstanceContainer;
    @Autowired
    private Structure structure;

    public AgentsUnit create(Position position) {
        log.debug("Initialize agents unit for position {}", position);
        if (!structure.belongsToStructure(position)) {
            // TODO: create own hierarchy of exceptions to runtime module (maybe after it's name change)
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }

        return new AgentsUnit(position, newPopulationController(), newCommonContextAdapter(position));
    }


    private UnitPopulationController newPopulationController() {
        return beanFactory.getBean(UnitPopulationController.class, mutableInstanceContainer.newChildContainer());
    }

    private CommonContextAdapter newCommonContextAdapter(Position position) {
        return beanFactory.getBean(CommonContextAdapter.class, position);
    }

}
