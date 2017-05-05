package org.hage.platform.node.executors.config.internal;

import lombok.Data;

@Data
public class ThreadingPolicyConfiguration {
    private final ThreadingPolicyType policyType;
    private final Integer fixedNumber;
}
