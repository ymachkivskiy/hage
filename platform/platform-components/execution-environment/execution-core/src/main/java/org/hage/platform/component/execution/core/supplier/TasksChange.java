package org.hage.platform.component.execution.core.supplier;

import lombok.Getter;
import org.hage.platform.component.execution.core.executor.PhasedRunnable;

import java.util.List;

import static java.util.Collections.emptyList;


@Getter
public class TasksChange {
    private final List<PhasedRunnable> toBeAdded;
    private final List<PhasedRunnable> toBeRemoved;

    public TasksChange(List<PhasedRunnable> toBeAdded, List<PhasedRunnable> toBeRemoved) {
        this.toBeAdded = (toBeAdded != null) ? toBeAdded : emptyList();
        this.toBeRemoved = (toBeRemoved != null) ? toBeRemoved : emptyList();
    }
}
