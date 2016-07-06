package org.hage.platform.component.runtime.unit;

public interface UnitComponent {
    void performPostConstruction();

    void setUnitContainer(UnitContainer unitContainer);
}
