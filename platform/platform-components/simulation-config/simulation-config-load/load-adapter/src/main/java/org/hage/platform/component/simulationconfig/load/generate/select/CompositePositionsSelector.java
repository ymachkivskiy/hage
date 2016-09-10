package org.hage.platform.component.simulationconfig.load.generate.select;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData;
import org.hage.platform.component.simulationconfig.load.definition.agent.SelectionMode;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CompositePositionsSelector implements PositionsSelector {

    private final static PositionsSelector NOT_IMPLEMENTED_SELECTOR = (chunk, selectionData) -> {
        log.warn("Selector not implemented for mode {}", selectionData.getMode());
        throw new NotImplementedException("Positions selector not implemented for mode " + selectionData.getMode());
    };

    private final Map<SelectionMode, PositionsSelector> selectorsMap = new EnumMap<>(SelectionMode.class);

    @Override
    public Set<Position> select(Chunk chunk, PositionsSelectionData selectionData) {
        return selectorsMap.getOrDefault(selectionData.getMode(), NOT_IMPLEMENTED_SELECTOR).select(chunk, selectionData);
    }

    private void setSelectorForMode(PositionsSelector selector, SelectionMode mode) {
        log.debug("Set position selector {} for mode {}", selector, mode);
        selectorsMap.put(mode, selector);
    }

    public void setAllSelector(PositionsSelector positionsSelector) {
        setSelectorForMode(positionsSelector, SelectionMode.ALL);
    }

    public void setRandomPositionsRandomNumberSelector(PositionsSelector positionsSelector) {
        setSelectorForMode(positionsSelector, SelectionMode.RANDOM_CHOOSE__RANDOM_NUMBER);
    }

    public void setRandomPositionsFixedNumberSelector(PositionsSelector positionsSelector) {
        setSelectorForMode(positionsSelector, SelectionMode.RANDOM_CHOOSE__FIXED_NUMBER);
    }

}
