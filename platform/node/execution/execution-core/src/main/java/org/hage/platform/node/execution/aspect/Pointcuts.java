package org.hage.platform.node.execution.aspect;

import org.aspectj.lang.annotation.Pointcut;

class Pointcuts {

    @Pointcut("execution(public boolean org.hage.platform.node.execution.step.StepTask.perform())")
    void stepPerforming() {
    }

    @Pointcut("execution(public void org.hage.platform.node.execution.phase.StepPhaseExecutor.executeStepPhase(..))")
    void stepPhaseExecution() {
    }

}
