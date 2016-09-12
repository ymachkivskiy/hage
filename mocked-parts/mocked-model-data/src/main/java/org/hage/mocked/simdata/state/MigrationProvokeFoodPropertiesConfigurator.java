package org.hage.mocked.simdata.state;

import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.structure.grid.Dimensions;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hage.mocked.simdata.state.Properties.FOOD;

public class MigrationProvokeFoodPropertiesConfigurator implements UnitPropertiesStateComponent {

    public static final long startingFoodAmount = 5_000;
    public static final int veryGoodPositionFoodAmount = 50_000;
    public static final long specialRarePriseAmount = 2_000;

    public static final int repeating = 20;

    private static final List<Chunk> chunks = asList(
        new Chunk(Position.position(0, 0, 0), Dimensions.definedBy(2, 2, 2)),
        new Chunk(Position.position(2, 2, 2), Dimensions.definedBy(2, 3, 2)),
        new Chunk(Position.position(0, 2, 0), Dimensions.definedBy(2, 2, 2)),
        new Chunk(Position.position(0, 0, 2), Dimensions.definedBy(2, 2, 2)),
        new Chunk(Position.position(2, 0, 0), Dimensions.definedBy(2, 3, 2)),
        new Chunk(Position.position(0, 2, 2), Dimensions.definedBy(2, 2, 2))
        );

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
                    if (chunks.get(((int) (stepNumber % chunks.size()))).containsPosition(unitPosition)) {
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
