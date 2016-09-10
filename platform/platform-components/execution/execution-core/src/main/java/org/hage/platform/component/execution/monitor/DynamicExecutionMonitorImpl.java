package org.hage.platform.component.execution.monitor;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.execution.phase.PhasesPreProcessor;
import org.hage.platform.component.runtime.migration.InternalMigrantsInformationProvider;
import org.hage.platform.component.runtime.unit.Unit;
import org.hage.platform.component.runtime.unit.registry.UnitRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.EnumMap;
import java.util.List;

import static java.time.Duration.ZERO;
import static java.util.stream.Collectors.toList;

@Slf4j
@SingletonComponent
class DynamicExecutionMonitorImpl implements NodeDynamicExecutionMonitor, ExecutionDurationObserver, PhasesPreProcessor {

    private final EnumMap<ExecutionPhaseType, Duration> phaseDurationMap = new EnumMap<>(ExecutionPhaseType.class);
    private Duration stepDuration = ZERO;

    @Autowired
    private UnitRegistry unitRegistry;
    @Autowired
    private InternalMigrantsInformationProvider migrantsInformationProvider;

    @Override
    public void beforeAllPhasesExecuted(long stepNumber) {
        phaseDurationMap.clear();
    }

    @Override
    public void refreshStepDuration(Duration duration) {
        log.debug("Refresh step duration to {}", duration);
        stepDuration = duration;
    }

    @Override
    public void refreshPhaseDuration(ExecutionPhaseType phaseType, Duration duration) {
        log.debug("Accept phase {} duration {}", phaseType, duration);
        phaseDurationMap.put(phaseType, duration);
    }

    @Override
    public DynamicExecutionInfo provideExecutionInfo() {
        return new DynamicExecutionInfo(
            new StepExecutionDurationInfo(stepDuration, phaseDurationMap),
            getAgentsInfo()
        );
    }

    private List<UnitAgentsNumberInfo> getAgentsInfo() {
        return unitRegistry.getAllUnits()
            .stream()
            .map(unit -> new UnitAgentsNumberInfo(unit.getPosition(), createAgentsInfoForUnit(unit)))
            .collect(toList());
    }

    private AgentsInfo createAgentsInfoForUnit(Unit unit) {
        log.debug("Create agents info for unit on {}", unit.getPosition());
        int numberOfMigrantsToUnit = migrantsInformationProvider.getNumberOfMigrantsTo(unit.getPosition());
        return unit.getInfo().addNumberOfAgentsToAdd(numberOfMigrantsToUnit);
    }


}
