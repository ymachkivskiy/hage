package org.hage.platform.component.execution.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public final class ExecutionInfo implements Serializable {
    @Getter
    private final long step;
}
