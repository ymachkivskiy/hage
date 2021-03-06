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

package org.hage.platform.util.reflect;


import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.primitives.Primitives;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.Primitives.wrap;


/**
 * Class related reflection utilities.
 *
 * @author AGH AgE Team
 * @since 2.6
 */
public final class Classes {

    private Classes() {
    }

    /**
     * Returns the hierarchy of the given class as a list, starting with that class and ending in Object.class.
     *
     * @param clazz the target class
     * @return the class hierarchy of the given class
     * @since 2.6
     */
    public static List<? extends Class<?>> classHierarchyOf(final Class<?> clazz) {
        Class<?> current = checkNotNull(clazz);
        final Builder<Class<?>> builder = ImmutableList.builder();
        while(current != null) {
            builder.add(current);
            current = current.getSuperclass();
        }
        return builder.build();
    }

    /**
     * Checks whether the two classes are equivalent when it comes to Autoboxing.
     *
     * @param first  the first class
     * @param second the second class
     * @return true if the classes are equal or either can be autoboxed into the other
     * @since 2.6
     */
    public static boolean equivalentWhenAutoBoxing(final Class<?> first, final Class<?> second) {
        return first.equals(second) || isWrapperOfPrimitive(first, second) || isWrapperOfPrimitive(second, first);
    }

    /**
     * Checks whether the first class is the wrapper of the primitive second class.
     *
     * @param wrapper   the wrapper class
     * @param primitive the primitive class
     * @return true if {@code primitive} is a primitive class and {@code wrapper} is its wrapper class
     * @since 2.6
     */
    public static boolean isWrapperOfPrimitive(final Class<?> wrapper, final Class<?> primitive) {
        return primitive.isPrimitive() && wrapper != primitive && Primitives.unwrap(wrapper) == primitive;
    }

    /**
     * Checks whether the classes contained in the first iterable can be assigned to references of the classes contained
     * in the second iterable. This method will return true if both iterables contain the same number of classes and
     * each pair of these is assignable, as of {@link Classes#isAssignable(Class, Class)}.
     *
     * @param source an iterable of sources
     * @param target an iterable of targets
     * @return true if classes of the first iterable are assignable to those of the second one.
     * @see Classes#isAssignable(Class, Class)
     * @since 2.6
     */
    public static boolean isAssignable(final Iterable<Class<?>> source, final Iterable<Class<?>> target) {
        return isAssignable(source.iterator(), target.iterator());
    }

    /**
     * Checks whether the classes returned by the first iterator can be assigned to references of the classes returned
     * by the second iterator. This method will return true if both iterators produce the same number of classes and
     * each pair of these is assignable, as of {@link Classes#isAssignable(Class, Class)}.
     *
     * @param source an iterator of sources
     * @param target an iterator of targets
     * @return true if the first sequence of classes is assignable to the second one.
     * @see Classes#isAssignable(Class, Class)
     * @since 2.6
     */
    public static boolean isAssignable(final Iterator<Class<?>> source, final Iterator<Class<?>> target) {
        while(source.hasNext() && target.hasNext()) {
            if(!isAssignable(source.next(), target.next())) {
                return false;
            }
        }
        return !(source.hasNext() || target.hasNext());
    }

    /**
     * Checks whether instances of the first class can be assigned to references of the second class, including
     * autoboxing.
     *
     * @param source the source class
     * @param target the target class
     * @return true if instances of the first class can be assigned to references of the second class, including
     * autoboxing
     * @see Class#isAssignableFrom(Class)
     * @since 2.6
     */
    public static boolean isAssignable(final Class<?> source, final Class<?> target) {
        return wrap(target).isAssignableFrom(wrap(source));
    }

    /**
     * Wraps the declared constructors of a given class in a type safe list.
     *
     * @param <T>   the type of the class
     * @param clazz the target class
     * @return a list of the declared constructors of the target class
     * @see Class#getDeclaredConstructors()
     * @since 2.6
     */
    public static <T> List<Constructor<T>> getDeclaredConstructors(final Class<T> clazz) {
        @SuppressWarnings("unchecked")
        // we have the type token
        final Constructor<T>[] declaredConstructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        return Arrays.asList(declaredConstructors);
    }

    /**
     * Returns an optional representing the default, empty constructor of a given class. The optional will be empty if
     * there is no such constructor. The constructor may have any access modifier.
     *
     * @param <T>   the type of the class
     * @param clazz the target class
     * @return an optional default constructor of the target class
     * @since 2.6
     */
    public static <T> Optional<Constructor<T>> getDeclaredDefaultConstructor(final Class<T> clazz) {
        try {
            return Optional.of(clazz.getDeclaredConstructor());
        } catch(final NoSuchMethodException e) {
            return Optional.absent();
        }
    }
}
