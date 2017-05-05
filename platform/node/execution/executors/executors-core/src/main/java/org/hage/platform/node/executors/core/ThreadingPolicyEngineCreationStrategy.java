package org.hage.platform.node.executors.core;

import org.hage.platform.node.executors.config.internal.ThreadingPolicyConfiguration;

interface ThreadingPolicyEngineCreationStrategy {
    boolean supports(ThreadingPolicyConfiguration configuration);

    ExecutionEngine create(ThreadingPolicyConfiguration configuration);
}
