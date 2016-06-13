package org.hage.platform.component.execution.step.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.ExecutionMonitor;
import org.hage.platform.component.execution.phase.PhasesPostProcessor;
import org.hage.platform.component.execution.phase.PhasesPreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.List;

import static java.util.Collections.emptyList;

@SingletonComponent
@Aspect
@Slf4j
@Order(1)
public class StepProcessorsAspect {

    @Autowired(required = false)
    private List<PhasesPreProcessor> preProcessors = emptyList();
    @Autowired(required = false)
    private List<PhasesPostProcessor> postProcessors = emptyList();
    @Autowired
    private ExecutionMonitor monitor;

    @Before("Pointcuts.stepPerforming()")
    private void beforeStep() {
        long stepNumber = monitor.getCurrentStepNumber();
        log.debug("Run {} step pre processors before step {} started", preProcessors.size(), stepNumber);

        for (PhasesPreProcessor preProcessor : preProcessors) {
            preProcessor.beforeAllPhasesExecuted(stepNumber);
        }
    }


    @After("Pointcuts.stepPerforming()")
    private void afterStep() {
        long stepNumber = monitor.getPerformedStepsCount();
        log.debug("Run {} step post processors after step {} finished", postProcessors.size(), stepNumber);

        for (PhasesPostProcessor postProcessor : postProcessors) {
            postProcessor.afterAllPhasesExecuted(stepNumber);
        }
    }
}
