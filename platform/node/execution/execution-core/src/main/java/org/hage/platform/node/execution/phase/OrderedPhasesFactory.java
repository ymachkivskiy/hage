package org.hage.platform.node.execution.phase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
public class OrderedPhasesFactory implements ExecutionPhaseFactory {

    private final List<ExecutionPhaseType> phaseTypesOrder;
    private List<ExecutionPhase> orderedPhases;

    @Autowired(required = false)
    private List<ExecutionPhase> allPhases = emptyList();

    public OrderedPhasesFactory(List<ExecutionPhaseType> phaseTypesOrder) {
        this.phaseTypesOrder = phaseTypesOrder;
    }

    @Override
    public List<ExecutionPhase> getFullCyclePhases() {
        return orderedPhases;
    }

    @PostConstruct
    private void preparePhasesList() {
        log.debug("Ordering execution phases according to types {}", phaseTypesOrder);

        Map<ExecutionPhaseType, List<ExecutionPhase>> phaseByType = allPhases
            .stream()
            .collect(groupingBy(ExecutionPhase::getType));

        orderedPhases = phaseTypesOrder.stream()
            .map(phaseType -> phaseByType.getOrDefault(phaseType, emptyList()))
            .flatMap(List::stream)
            .collect(toList());

        log.debug("Phases order is {}", orderedPhases);
    }

}
