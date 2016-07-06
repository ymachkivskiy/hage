package org.hage.platform.component.runtime.unit;

import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesController;

import static com.google.common.base.Preconditions.checkNotNull;

class UnitAssembler {

    private UnitLocationController locationCtrl;
    private UnitActivePopulationController unitActivePopulationCtrl;
    private UnitComponentCreationController unitComponentCreationCtrl;
    private UnitPropertiesController unitPropertiesController;
    private AgentContextAdapter agentContextAdapter;
    private UnitContainer componentsCommon;

    private UnitAssembler() {
    }

    public static UnitAssembler unitAssembler() {
        return new UnitAssembler();
    }

    public UnitAssembler withLocationCtrl(UnitLocationController locationCtrl) {
        this.locationCtrl = locationCtrl;
        return this;
    }

    public UnitAssembler withUnitActivePopulationCtrl(UnitActivePopulationController unitActivePopulationCtrl) {
        this.unitActivePopulationCtrl = unitActivePopulationCtrl;
        return this;
    }

    public UnitAssembler withUnitComponentCreationCtrl(UnitComponentCreationController unitComponentCreationCtrl) {
        this.unitComponentCreationCtrl = unitComponentCreationCtrl;
        return this;
    }

    public UnitAssembler withUnitPropertiesController(UnitPropertiesController unitPropertiesController) {
        this.unitPropertiesController = unitPropertiesController;
        return this;
    }

    public UnitAssembler withAgentContextAdapter(AgentContextAdapter agentContextAdapter) {
        this.agentContextAdapter = agentContextAdapter;
        return this;
    }

    public UnitAssembler withUnitContainer(UnitContainer componentsCommon) {
        this.componentsCommon = componentsCommon;
        return this;
    }

    public void assemble(AgentsUnit unit) {

        UnitLocationController locationCtrl = checkNotNull(this.locationCtrl, "Location controller not specified");
        UnitActivePopulationController unitActivePopulationCtrl = checkNotNull(this.unitActivePopulationCtrl, "Active population controller not specified");
        UnitComponentCreationController unitComponentCreationCtrl = checkNotNull(this.unitComponentCreationCtrl, "Component creation controller not specified");
        UnitPropertiesController unitPropertiesController = checkNotNull(this.unitPropertiesController, "Unit properties controller not specified");
        AgentContextAdapter agentContextAdapter = checkNotNull(this.agentContextAdapter, "Agent context adapter not specified");
        UnitContainer componentsCommon = checkNotNull(this.componentsCommon, "Unit components common not specified");

        unit.setUnitPropertiesController(unitPropertiesController);
        unit.setUnitLocationController(locationCtrl);
        unit.setUnitComponentCreationController(unitComponentCreationCtrl);
        unit.setUnitActivePopulationController(unitActivePopulationCtrl);
        unit.setAgentContextAdapter(agentContextAdapter);

        // TODO: generify & loop

        unitActivePopulationCtrl.setUnitContainer(componentsCommon);
        locationCtrl.setUnitContainer(componentsCommon);
        unitActivePopulationCtrl.setUnitContainer(componentsCommon);
        unitComponentCreationCtrl.setUnitContainer(componentsCommon);
        unitPropertiesController.setUnitContainer(componentsCommon);

        locationCtrl.performPostConstruction();
        unitActivePopulationCtrl.performPostConstruction();
        unitComponentCreationCtrl.performPostConstruction();
        unitPropertiesController.performPostConstruction();
    }
}
