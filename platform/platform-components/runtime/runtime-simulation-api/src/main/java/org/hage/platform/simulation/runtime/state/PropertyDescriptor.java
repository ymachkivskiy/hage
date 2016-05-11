package org.hage.platform.simulation.runtime.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class PropertyDescriptor<T extends Serializable> implements Serializable {
    private final String name;
    private final Class<T> type;

    private PropertyDescriptor(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <T extends Serializable> PropertyDescriptor<T> propDescriptorOf(String name, Class<T> type) {
        return new PropertyDescriptor<>(name, type);
    }

}
