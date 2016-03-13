package org.hage.platform.component.lifecycle.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleEngine;
import org.hage.platform.util.connection.ClusterAddressManager;
import org.hage.platform.util.connection.ClusterMemberChangeCallback;
import org.hage.platform.util.connection.NodeAddress;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.FAIL;

@Slf4j
public class ClusterConsistencyGuard implements ClusterMemberChangeCallback {

    @Autowired
    private ClusterAddressManager addressManager;
    @Autowired
    private LifecycleEngine lifecycleEngine;

    @Override
    public void onMemberAdd(NodeAddress newMember) { /* no-op */ }

    @Override
    public void onMemberRemoved(NodeAddress removedMember) {
        log.error("Member '{}' removed from cluster", removedMember);
        lifecycleEngine.performCommand(FAIL);
    }

    @PostConstruct
    private void init() {
        addressManager.addMembershipChangeCallback(this);
    }

}
