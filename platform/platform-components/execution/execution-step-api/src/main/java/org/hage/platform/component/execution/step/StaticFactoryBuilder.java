package org.hage.platform.component.execution.step;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.hage.util.CollectionUtils.nullSafeCopy;

public class StaticFactoryBuilder {

    private List<IndependentPhasesGroup> independentPhaseGroups;

    private StaticFactoryBuilder() {
        independentPhaseGroups = new ArrayList<>();
    }

    public static StaticFactoryBuilder staticFactoryBuilder() {
        return new StaticFactoryBuilder();
    }

    public StaticFactoryBuilder addNextIndependentPhases(StepPhase firstPhase, StepPhase... otherPhases) {
        independentPhaseGroups.add(new IndependentPhasesGroup(ImmutableList.<StepPhase>builder().add(firstPhase).add(otherPhases).build()));
        return this;
    }

    public StepPhaseFactory build() {
        return new Factory(unmodifiableList(nullSafeCopy(independentPhaseGroups)));
    }


    @RequiredArgsConstructor
    private static class Factory implements StepPhaseFactory {
        @Getter
        private final List<IndependentPhasesGroup> fullCyclePhasesGroups;
    }


}
