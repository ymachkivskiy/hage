package org.hage.example.state;

import org.hage.platform.simulation.runtime.state.PropertyDescriptor;

import static org.hage.platform.simulation.runtime.state.PropertyDescriptor.propDescriptorOf;

public abstract class Properties {

    public static final PropertyDescriptor<Double> TEMPERATURE = propDescriptorOf("temperature", Double.class);

    public static final PropertyDescriptor<Integer> ALGAE = propDescriptorOf("algae", Integer.class);

    public static final PropertyDescriptor<CustomState> STATE = propDescriptorOf("state", CustomState.class);

}
