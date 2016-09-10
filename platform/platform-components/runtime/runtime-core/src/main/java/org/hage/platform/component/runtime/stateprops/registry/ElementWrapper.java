package org.hage.platform.component.runtime.stateprops.registry;

import lombok.Getter;
import org.hage.platform.simulation.runtime.state.descriptor.PropertyDescriptor;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Getter
class ElementWrapper implements Serializable {
    private static final ElementWrapper EMPTY = new ElementWrapper(null);

    private final Serializable element;

    private ElementWrapper(Serializable element) {
        this.element = element;
    }

    public <T extends Serializable> Optional<T> toOptionalUsing(PropertyDescriptor<T> descriptor) {
        if (element != null) {
            return of(descriptor.cast(element));
        } else {
            return empty();
        }
    }

    public <T extends Serializable> ElementWrapper createCopyUsing(PropertyDescriptor<T> descriptor) {
        if (element == null) {
            return this;
        } else {
            return ofSerializable(descriptor.getReadViewFor(descriptor.cast(element)));
        }
    }

    public <T extends Serializable> ElementWrapper mapped(PropertyDescriptor<T> descriptor, UnaryOperator<T> unaryOperator) {
        if (element == null) {
            return this;
        }else{
            return ofSerializable(unaryOperator.apply(descriptor.cast(element)));
        }
    }

    public static ElementWrapper ofSerializable(Serializable element) {
        return new ElementWrapper(checkNotNull(element));
    }

    public static ElementWrapper emptyWrapper() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return "ElementWrapper(element=" + element + ")";
    }
}
