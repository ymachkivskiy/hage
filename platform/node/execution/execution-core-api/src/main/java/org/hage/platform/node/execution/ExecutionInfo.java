package org.hage.platform.node.execution;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public final class ExecutionInfo implements Serializable {
    @Getter
    private final long step;
}
