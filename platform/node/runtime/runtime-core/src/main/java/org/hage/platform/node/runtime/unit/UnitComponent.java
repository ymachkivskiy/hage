package org.hage.platform.node.runtime.unit;

public interface UnitComponent {
    void performPostConstruction();

    void setUnitContainer(UnitContainer unitContainer);
}
