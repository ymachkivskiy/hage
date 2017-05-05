package org.hage.platform.node.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.execution.phase.ExecutionPhaseType;
import org.hage.platform.node.runtime.step.StepFinalizer;
import org.hage.platform.node.runtime.unit.Unit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hage.platform.node.execution.phase.ExecutionPhaseType.POST__FINALIZATION;


@SingletonComponent
public class StepFinalizationPhase extends AbstractUnitPhase {

    @Autowired(required = false)
    private List<StepFinalizer> stepFinalizers = emptyList();

    @Override
    public ExecutionPhaseType getType() {
        return POST__FINALIZATION;
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::postProcess;
    }

    @Override
    public Collection<? extends Runnable> getTasks(long currentStep) {
        ArrayList<Runnable> resultRunnable = new ArrayList<>(super.getTasks(currentStep));

        resultRunnable.addAll(getExtraRunnable());

        return resultRunnable;
    }

    private List<Runnable> getExtraRunnable() {
        return stepFinalizers.stream()
            .map(stepPostProcessor -> (Runnable) stepPostProcessor::finalizeStep)
            .collect(toList());
    }
}
