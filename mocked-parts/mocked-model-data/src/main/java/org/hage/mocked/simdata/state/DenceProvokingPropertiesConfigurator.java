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


    private static final int PROFIT_PLUS = 1000;
    private static final int SIMPLE_PLUS = 100;

    private static final List<Chunk> profitChunks = asList(
        new Chunk(position(0, 0, 1), definedBy(2, 2, 1)),
        new Chunk(position(5, 5, 8), definedBy(1, 3, 3)),
        new Chunk(position(2, 3, 4), definedBy(1, 1, 2)),
        new Chunk(position(3, 7, 0), definedBy(4, 3, 1))
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

            if (stepNumber % 2 == 0) {
                if (isPoorPosition(unitPosition)) {
                    readWriteUnitProperties.set(FOOD, 0L);
                } else {
                    readWriteUnitProperties.updateAndGet(FOOD, f -> f + SIMPLE_PLUS);
                }

            } else {
                if (isProfitPosition(unitPosition)) {
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
