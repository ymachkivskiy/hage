package org.hage.mocked.simdata.state;

import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;
import org.hage.platform.simulation.runtime.state.property.ReadWriteUnitProperties;
import org.hage.platform.simulation.runtime.state.property.WriteUnitProperties;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hage.mocked.simdata.state.Properties.FOOD;
import static org.hage.platform.component.structure.Position.position;
import static org.hage.platform.component.structure.grid.Dimensions.definedBy;

public class DenceProvokingPropertiesConfigurator implements UnitPropertiesStateComponent {


    private static final int PROFIT_PLUS = 10000;
    private static final int SIMPLE_PLUS = 50;
    private static final int BAD_SEASON_MINUS = 1500;

    private static final List<Chunk> profitChunks = asList(
        new Chunk(position(0, 0, 1), definedBy(1, 1, 3)),
        new Chunk(position(5, 5, 5), definedBy(1, 1, 1))
    );


    private static final List<Chunk> poorChunks = asList(
        new Chunk(position(4, 3, 2), definedBy(2, 2, 2)),
        new Chunk(position(1, 0, 2), definedBy(5, 3, 4)),
        new Chunk(position(5, 3, 7), definedBy(3, 5, 1)),
        new Chunk(position(5, 5, 8), definedBy(1, 3, 3))
    );

    @Override
    public void updateProperties(ReadWriteUnitProperties readWriteUnitProperties, Position unitPosition, long stepNumber) {

        if (stepNumber == 1) {
            initProperties(readWriteUnitProperties);
        } else {

            long month = stepNumber % 12;

            if (month == 2 || month == 5 || month == 10) {

                readWriteUnitProperties.updateAndGet(FOOD,
                    currentFood -> currentFood >= BAD_SEASON_MINUS ? currentFood - BAD_SEASON_MINUS : 0L
                );

            } else {

                if (stepNumber % 5 == 0 || (stepNumber % 3 == 0 && isPoorPosition(unitPosition))) {
                    readWriteUnitProperties.set(FOOD, 0L);
                } else if (isProfitPosition(unitPosition)) {
                    readWriteUnitProperties.updateAndGet(FOOD, f -> f + PROFIT_PLUS);
                } else {
                    readWriteUnitProperties.updateAndGet(FOOD, f -> f + SIMPLE_PLUS);
                }

            }


        }

    }

    private boolean isPoorPosition(Position position) {

        for (Chunk poorChunk : poorChunks) {
            if (poorChunk.containsPosition(position)) {
                return true;
            }
        }

        return false;
    }

    private boolean isProfitPosition(Position position) {

        for (Chunk poorChunk : profitChunks) {
            if (poorChunk.containsPosition(position)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<PropertyDescriptor> getRegisteredProperties() {
        return singletonList(FOOD);
    }

    private void initProperties(WriteUnitProperties readWriteUnitProperties) {
        readWriteUnitProperties.set(FOOD, 300L);
    }

}
