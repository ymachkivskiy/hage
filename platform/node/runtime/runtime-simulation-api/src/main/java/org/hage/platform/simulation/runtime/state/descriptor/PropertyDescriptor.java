package org.hage.platform.simulation.runtime.state.descriptor;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
public abstract class PropertyDescriptor<T extends Serializable> implements Serializable {

    @Getter
    private final String name;
    private final Class<T> type;

    protected PropertyDescriptor(String name, Class<T> type) {
        this.name = checkNotNull(name);
        this.type = checkNotNull(type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(name=" + name + ", type=" + type.getName() + ")";
    }

    public abstract T getReadViewFor(T original);

    public T cast(Object object) {
        return type.cast(object);
    }

    public static <T extends Serializable> PropertyDescriptor<T> primitiveProperty(String name, Class<T> type) {
        return new ImmutablePropertyDescriptor<>(name, type);
    }

    public static <T extends Serializable & CloneableValue<T>> PropertyDescriptor<T> readSafeProperty(String name, Class<T> type) {
        return new MutablePropertyDescriptor<>(name, type);
    }

    private static class ImmutablePropertyDescriptor<T extends Serializable> extends PropertyDescriptor<T> implements Serializable {

        public ImmutablePropertyDescriptor(String name, Class<T> type) {
            super(name, type);
        }

        @Override
        public T getReadViewFor(T original) {
            return original;
        }

    }

    private static class MutablePropertyDescriptor<T extends Serializable & CloneableValue<T>> extends PropertyDescriptor<T> implements Serializable {

        public MutablePropertyDescriptor(String name, Class<T> type) {
            super(name, type);
        }

        @Override
        public T getReadViewFor(T original) {
            return original.createClone();
        }
    }

}





