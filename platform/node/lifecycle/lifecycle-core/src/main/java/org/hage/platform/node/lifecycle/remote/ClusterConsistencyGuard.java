package org.hage.platform.node.lifecycle.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.ClusterManager;
import org.hage.platform.cluster.api.ClusterMemberChangeCallback;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.node.lifecycle.BaseLifecycleCommand;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Slf4j
public class ClusterConsistencyGuard implements ClusterMemberChangeCallback {

    @Autowired
    private ClusterManager addressManager;
    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    @Override
    public void onMemberRemoved(NodeAddress removedMember) {
        log.error("Member '{}' removed from cluster", removedMember);
        lifecycleCommandInvoker.invokeCommand(BaseLifecycleCommand.ASYNC_FAIL);
    }

    @PostConstruct
    private void init() {
        addressManager.addMembershipChangeCallback(this);
    }

}
