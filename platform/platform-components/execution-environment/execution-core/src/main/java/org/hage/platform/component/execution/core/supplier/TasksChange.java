package org.hage.platform.component.execution.core.supplier;

import lombok.Getter;
import org.hage.platform.component.execution.core.executor.PhasedRunnable;

import java.util.Collection;

import static java.util.Collections.emptyList;


@Getter
public class TasksChange {
    private final Collection<PhasedRunnable> toBeAdded;
    private final Collection<PhasedRunnable> toBeRemoved;

    public TasksChange(Collection<PhasedRunnable> toBeAdded, Collection<PhasedRunnable> toBeRemoved) {
        this.toBeAdded = (toBeAdded != null) ? toBeAdded : emptyList();
        this.toBeRemoved = (toBeRemoved != null) ? toBeRemoved : emptyList();
    }
}
