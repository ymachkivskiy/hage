package org.hage.platform.node.executors.schedule;

public interface ScheduleTask {
    /**
     * Perform task and return indicating that some work was performed at all
     *
     * @return true if task was performed, false otherwise. If all task in scheduler will not be performed, scheduler will
     * suspend till some of tasks will be resumed
     */
    boolean perform();
}
