package org.hage.platform.component.execution.monitor;

import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;

import java.io.Serializable;
import java.time.Duration;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static java.time.Duration.ZERO;
import static org.hage.util.CollectionUtils.nullSafe;

@ToString
public class StepExecutionDurationInfo implements Serializable {

    @Getter
    private final Duration summaryStepDuration;
    private final EnumMap<ExecutionPhaseType, Duration> phaseDurationMap;

    public StepExecutionDurationInfo(Duration summaryStepDuration, Map<ExecutionPhaseType, Duration> phaseDurationMap) {
        this.summaryStepDuration = summaryStepDuration;
        this.phaseDurationMap = new EnumMap<>(nullSafe(phaseDurationMap));
    }

    public Duration getDurationOf(ExecutionPhaseType phaseType, ExecutionPhaseType... otherPhasesTypes) {
        return EnumSet.of(phaseType, otherPhasesTypes)
            .stream()
            .map(pt -> phaseDurationMap.getOrDefault(pt, ZERO))
            .reduce(ZERO, Duration::plus);
    }

    public String toJson() {

        StringBuilder sb = new StringBuilder();

        sb.append("{")
            .append("\"stepDurationNanos\":").append(summaryStepDuration.toNanos()).append(",")
            .append("\"phasesDurationsNanosMap\":{");

        for (Map.Entry<ExecutionPhaseType, Duration> e : phaseDurationMap.entrySet()) {
            sb.append("\"").append(e.getKey()).append("\":").append(e.getValue().toNanos()).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);

        sb.append("}}");

        return sb.toString();
    }

}
