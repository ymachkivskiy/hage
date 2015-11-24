package org.hage.platform.config.def.agent;

import lombok.Getter;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;


public final class AgentCountData {
    private static final AgentCountData RANDOM = new AgentCountData(AgentCountMode.RANDOM, 1);

    @Getter
    private final AgentCountMode agentCountMode;
    @Getter
    private final Optional<Integer> value;
    @Getter
    private final Optional<Integer> secondaryValue;

    private AgentCountData(AgentCountMode agentCountMode, Integer value, Integer secondaryValue) {
        checkArgument(value == null || value > 0, "Primary value should be greater than zero but is " + value);
        checkArgument(secondaryValue == null || secondaryValue > 0, "Secondary value should be greater than zero but is " + secondaryValue);

        this.agentCountMode = agentCountMode;
        this.value = ofNullable(value);
        this.secondaryValue = ofNullable(secondaryValue);
    }

    private AgentCountData(AgentCountMode agentCountMode, Integer value) {
        this(agentCountMode, value, null);
    }

    public static AgentCountData fixed(int number) {
        return new AgentCountData(AgentCountMode.FIXED, number);
    }

    public static AgentCountData atLeast(int number) {
        return new AgentCountData(AgentCountMode.AT_LEAST, number);
    }

    public static AgentCountData atMost(int number) {
        return new AgentCountData(AgentCountMode.AT_MOST, number);
    }

    public static AgentCountData between(int lowerBound, int upperBound) {
        checkArgument(lowerBound <= upperBound);
        return new AgentCountData(AgentCountMode.BETWEEN, lowerBound, upperBound);
    }

    public static AgentCountData random() {
        return RANDOM;
    }
}
