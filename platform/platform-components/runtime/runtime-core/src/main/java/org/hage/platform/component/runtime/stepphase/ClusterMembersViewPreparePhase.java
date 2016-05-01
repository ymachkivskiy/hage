package org.hage.platform.component.runtime.stepphase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.step.SingleRunnableStepPhase;
import org.hage.platform.component.runtime.cluster.ClusterMembersStepViewPreparer;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
public class ClusterMembersViewPreparePhase extends SingleRunnableStepPhase {

    @Autowired
    private ClusterMembersStepViewPreparer preparer;

    @Override
    public String getPhaseName() {
        return "Cluster members view preparation";
    }

    @Override
    protected void executePhase(long currentStep) {
        preparer.prepareView();
    }

}
