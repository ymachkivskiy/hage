package org.hage.mocked.simdata.state;

import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hage.mocked.simdata.state.Properties.FOOD;
import static org.hage.platform.component.structure.Position.position;
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;

public class MigrationProvokeFoodPropertiesConfigurator implements UnitPropertiesStateComponent {

    public static final long startingFoodAmount = 5_000;
    public static final int veryGoodPositionFoodAmount = 20_000;
    public static final long specialRarePriseAmount = 2_000;

    public static final int repeating = 3;

    private static final List<Position> allPositions = new ArrayList<>(new Chunk(position(0, 0, 0), definedBy(5, 5, 5)).getInternalPositions());

    private int counter = 0;


    @Override
    public void updateProperties(ReadWriteUnitProperties readWriteUnitProperties, Position unitPosition, long stepNumber) {

        if (stepNumber == 1) {
            initializeProps(readWriteUnitProperties);
        } else {

            if (stepNumber % 13 == 0) {
                readWriteUnitProperties.updateAndGet(FOOD, currentFoodAmount -> currentFoodAmount + specialRarePriseAmount);
            } else {
                counter = (counter + 1) % repeating;

                if (counter == 0) {
                    if (allPositions.get(((int) (stepNumber % allPositions.size()))).equals(unitPosition)) {
                        readWriteUnitProperties.updateAndGet(FOOD, currFood -> currFood + veryGoodPositionFoodAmount);
                    }
                }

            }
        }
    }


    @Override
    public List<PropertyDescriptor> getRegisteredProperties() {
        return Collections.singletonList(FOOD);
    }

    private void initializeProps(ReadWriteUnitProperties readWriteUnitProperties) {

        readWriteUnitProperties.set(FOOD, startingFoodAmount);
    }

}
