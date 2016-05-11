package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.Unit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;


@SingletonComponent
public class StepPostProcessPhase extends AbstractUnitPhase {

    @Autowired(required = false)
    private List<StepPostProcessor> stepPostProcessors = emptyList();

    @Override
    public String getPhaseName() {
        return "Step post process";
    }

    @Override
    protected Runnable extractRunnable(Unit unit) {
        return unit::postProcess;
    }

    @Override
    public Collection<? extends Runnable> getRunnable(long currentStep) {
        ArrayList<Runnable> resultRunnable = new ArrayList<>(super.getRunnable(currentStep));

        resultRunnable.addAll(getExtraRunnable());

        return resultRunnable;
    }

    private List<Runnable> getExtraRunnable() {
        return stepPostProcessors.stream()
            .map(stepPostProcessor -> (Runnable) stepPostProcessor::afterStepPerformed)
            .collect(toList());
    }
}
