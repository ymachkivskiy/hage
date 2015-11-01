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
 * Created: 2012-03-15
 * $Id$
 */

package org.hage.platform.reflect.predicates;


import com.google.common.base.Predicate;
import org.hage.platform.reflect.Constructors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;


/**
 * Utility predicates for Constructors.
 *
 * @author AGH AgE Team
 * @since 2.6
 */
public class ConstructorPredicates {

    /**
     * Returns a predicate checking if constructors match a list of actual parameters types.
     *
     * @param actualParameterTypes the list of actual parameters types
     * @return a predicate
     * @see Constructors#isMatchingActualParameters(Constructor, List)
     * @since 2.6
     */
    public static Predicate<Constructor<?>> matchingActualParameters(final List<Class<?>> actualParameterTypes) {
        return input -> Constructors.isMatchingActualParameters(input, actualParameterTypes);
    }

    /**
     * Returns a predicate checking if constructors are annotated with a given annotation.
     *
     * @param annotation the annotation
     * @return a predicate
     * @since 2.6
     */
    public static Predicate<Constructor<?>> withAnnotation(final Class<? extends Annotation> annotation) {
        return input -> input.isAnnotationPresent(annotation);
    }
}
