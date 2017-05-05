package org.hage.platform.component.lifecycle.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.ClusterManager;
import org.hage.platform.cluster.api.ClusterMemberChangeCallback;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.ASYNC_FAIL;

@Slf4j
public class ClusterConsistencyGuard implements ClusterMemberChangeCallback {

    @Autowired
    private ClusterManager addressManager;
    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    @Override
    public void onMemberRemoved(NodeAddress removedMember) {
        log.error("Member '{}' removed from cluster", removedMember);
        lifecycleCommandInvoker.invokeCommand(ASYNC_FAIL);
    }

    @PostConstruct
    private void init() {
        addressManager.addMembershipChangeCallback(this);
    }

}
