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

package org.hage.platform.util.reflect.attribute;


/**
 * Common abstraction for reflection based injection. May represent a field or a method.
 *
 * @param <T> the type of this attribute
 * @author AGH AgE Team
 * @since 2.6
 */
public interface Attribute<T> {

    /**
     * Returns the name of this attribute.
     *
     * @return this attribute's name
     * @since 2.6
     */
    String getName();

    /**
     * Returns the type of this attribute.
     *
     * @return this attribute's type
     * @since 2.6
     */
    Class<T> getType();

    /**
     * Sets this attribute on the given target to the given value.
     *
     * @param target the target to be set on
     * @param value  the value to be set to
     * @throws NullPointerException     if the target is null
     * @throws IllegalArgumentException if the value is not an instance of the attribute type or some exception happens.
     * @throws IllegalAccessException   if the underlying attribute is inaccessible
     * @since 2.6
     */
    void setValue(Object target, T value) throws IllegalAccessException;

    /**
     * Provides a type safe generic view of this attribute. Usage:
     * <p>
     * <pre>
     * Attribute<?> someAttribute = // initialize this somehow
     * if(String.class.equals(someAttribute.getType())){
     *     someAttribute.asType(String.class).setValue(bean, "Hello World");
     * }
     * </pre>
     *
     * @param <S>        the target type
     * @param targetType the target type class
     * @return this attribute, casted to the supplied generic type
     * @throws IllegalArgumentException if the supplied type is not compatible with {@link #getType()}
     * @since 2.6
     */
    <S> Attribute<S> asType(final Class<S> targetType);
}
