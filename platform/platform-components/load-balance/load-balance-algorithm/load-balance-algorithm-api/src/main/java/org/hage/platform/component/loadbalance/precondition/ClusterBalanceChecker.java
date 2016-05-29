package org.hage.platform.component.loadbalance.precondition;

import java.util.List;

public interface ClusterBalanceChecker {
    // TODO: document
    boolean isBalanced(List<NodeDynamicStats> nodeDynamicStats);
}
