package org.hage.mocked.simdata.state;

import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import static org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor.primitiveProperty;
import static org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor.readSafeProperty;

public abstract class Properties {

    public static final PropertyDescriptor<Double> TEMPERATURE = primitiveProperty("temperature", Double.class);

    public static final PropertyDescriptor<Integer> ALGAE = primitiveProperty("algae", Integer.class);

    public static final PropertyDescriptor<CustomState> STATE = readSafeProperty("state", CustomState.class);

}
