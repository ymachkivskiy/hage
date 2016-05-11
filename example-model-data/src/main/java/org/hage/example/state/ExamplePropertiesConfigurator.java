package org.hage.example.state;

import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.state.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.ReadWriteUnitProperties;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hage.example.state.Properties.*;

public class ExamplePropertiesConfigurator implements UnitPropertiesStateComponent {


    @Override
    public void updateProperties(ReadWriteUnitProperties readWriteUnitProperties, Position unitPosition, long stepNumber) {

        if (stepNumber == 1) {
            initProperties(readWriteUnitProperties);
        } else {

            if (stepNumber % 4 == 0) {
                readWriteUnitProperties.updateAndGetProperty(STATE, cs -> cs.withIncreasedAmount(500));
            }

            readWriteUnitProperties.updateAndGetProperty(
                TEMPERATURE,
                oldTemp -> oldTemp + unitPosition.vertical * (1 + stepNumber));

            readWriteUnitProperties.tryUpdateProperty(
                ALGAE,
                currAlgae -> currAlgae < 100,
                currAlgae -> currAlgae + 20);
        }

    }

    @Override
    public List<PropertyDescriptor> getRegisteredProperties() {
        return asList(ALGAE, TEMPERATURE, STATE);
    }

    private void initProperties(ReadWriteUnitProperties readWriteUnitProperties) {
        CustomState state = new CustomState();
        state.setAlgaeAmount(1000);

        readWriteUnitProperties.setProperty(STATE, state);
        readWriteUnitProperties.setProperty(TEMPERATURE, 36.6);
        readWriteUnitProperties.setProperty(ALGAE, 200);
    }

}
