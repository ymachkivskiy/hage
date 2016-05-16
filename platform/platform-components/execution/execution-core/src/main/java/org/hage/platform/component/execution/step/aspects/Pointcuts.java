package org.hage.platform.component.execution.step.aspects;

import org.aspectj.lang.annotation.Pointcut;

class Pointcuts {

    @Pointcut("execution(public void org.hage.platform.component.execution.step.ExecutionStepRunnable.run())")
    void stepPerforming() {}

}
