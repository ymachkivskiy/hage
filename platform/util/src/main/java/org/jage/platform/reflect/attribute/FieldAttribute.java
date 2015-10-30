/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2012-02-23
 * $Id$
 */

package org.jage.platform.reflect.attribute;


import com.google.common.base.Objects;

import java.lang.reflect.Field;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;


/**
 * Attribute implementation which injects values into a non-final instance field.
 * <p>
 * Is effectively immutable if the underlying field is not modified.
 *
 * @param <T> the type of this attribute
 * @author AGH AgE Team
 */
final class FieldAttribute<T> extends AbstractAttribute<T> {

    private final Field field;

    private FieldAttribute(final String name, final Class<T> type, final Field field) {
        super(name, type);
        this.field = checkNotNull(field);
    }

    /**
     * Creates an attribute based on the given field. The attribute name and type will be inferred from this field. The
     * field must not be final or static.
     *
     * @param field the field this attribute represents
     * @throws IllegalArgumentException if the field is final or static
     */
    @SuppressWarnings("unchecked")
    static <T> Attribute<T> newFieldAttribute(final Field field) {
        checkNotNull(field);
        checkArgument(!isFinal(field.getModifiers()), "The given field is final");
        checkArgument(!isStatic(field.getModifiers()), "The given field is static");

        return new FieldAttribute<T>(field.getName(), (Class<T>) field.getType(), field);
    }

    @Override
    public void setValue(final Object target, final T value) throws IllegalAccessException {
        field.setAccessible(true);
        // All the exceptions declared in Attribute will be handled by the call
        field.set(target, value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field, getName(), getType());
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof FieldAttribute)) {
            return false;
        }

        final FieldAttribute<?> other = (FieldAttribute<?>) obj;

        // name and type are inferred from the field, so doesn't need to be checked
        return Objects.equal(this.field, other.field);
    }
}
