package org.hage.mocked.simdata.state;

import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;
import org.hage.platform.simulation.runtime.state.property.WriteUnitProperties;

import java.util.List;

import static java.util.Arrays.asList;

public class ExamplePropertiesConfigurator implements UnitPropertiesStateComponent {

    private int age;

    @Override
    public void updateProperties(ReadWriteUnitProperties readWriteUnitProperties, Position unitPosition, long stepNumber) {

        if (stepNumber == 1) {
            initProperties(readWriteUnitProperties);
        } else {

            if (stepNumber % 4 == 0) {
                readWriteUnitProperties.updateAndGet(Properties.STATE, cs -> cs.withIncreasedAmount(500));
            }

            readWriteUnitProperties.updateAndGet(
                Properties.TEMPERATURE,
                oldTemp -> oldTemp + unitPosition.vertical * (1 + stepNumber));

            readWriteUnitProperties.tryUpdate(
                Properties.ALGAE,
                currAlgae -> currAlgae < 100,
                currAlgae -> currAlgae + 20);
        }

        age++;
    }

    @Override
    public List<PropertyDescriptor> getRegisteredProperties() {
        return asList(Properties.ALGAE, Properties.TEMPERATURE, Properties.STATE);
    }

    private void initProperties(WriteUnitProperties readWriteUnitProperties) {
        CustomState state = new CustomState();
        state.setAlgaeAmount(1000);

        readWriteUnitProperties.set(Properties.STATE, state);
        readWriteUnitProperties.set(Properties.TEMPERATURE, 36.6);
        readWriteUnitProperties.set(Properties.ALGAE, 200);
    }

}
