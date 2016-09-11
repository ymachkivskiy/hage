package org.hage.mocked.simdata.state;

import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import static org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor.primitiveProperty;

public abstract class Properties {

    public static final PropertyDescriptor<Long> FOOD = primitiveProperty("food", Long.class);

}
