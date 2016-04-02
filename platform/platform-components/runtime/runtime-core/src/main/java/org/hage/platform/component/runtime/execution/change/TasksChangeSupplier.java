package org.hage.platform.component.runtime.execution.change;

public interface TasksChangeSupplier {
    TasksChange pollCurrentChange();
}
