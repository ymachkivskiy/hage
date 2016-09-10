package org.hage.platform.component.execution.aspect;

import org.aspectj.lang.annotation.Pointcut;

class Pointcuts {

    @Pointcut("execution(public boolean org.hage.platform.component.execution.step.StepTask.perform())")
    void stepPerforming() {
    }

    @Pointcut("execution(public void org.hage.platform.component.execution.phase.StepPhaseExecutor.executeStepPhase(..))")
    void stepPhaseExecution() {
    }

}
