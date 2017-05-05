package org.hage.platform.node.execution.phase;

public enum ExecutionPhaseType {
    MAIN__AGENTS_STEP("Agents step"),
    MAIN___CONTROL_AGENT_STEP("Control agents step"),
    POST__EXTERNAL_MIGRATION_PROCESSING("External migration"),
    PRE__INTERNAL_MIGRATION_PROCESSING("Internal migration"),
    POST__FINALIZATION("Step finalization"),
    PRE__STRUCTURE_DISTRIBUTION("Structure distribution"),
    PRE__UNIT_PROPERTIES_UPDATE("Unit properties update"),
    PRE__UNITS_UNPACKING("Units unpacking"),
    SYNC("Synchronization"),
    PRE__SHARE_UPDATED_UNIT_PROPERTIES("Unit properties sharing"),
    MAIN__CONDITION_CHECK("Stop condition check"),;

    private final String description;

    ExecutionPhaseType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
