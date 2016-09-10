package org.hage.platform.util.executors.core;

import org.hage.platform.util.executors.config.internal.ThreadingPolicyConfiguration;

interface ThreadingPolicyEngineCreationStrategy {
    boolean supports(ThreadingPolicyConfiguration configuration);

    ExecutionEngine create(ThreadingPolicyConfiguration configuration);
}
