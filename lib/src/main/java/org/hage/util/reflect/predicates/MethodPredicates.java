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
 * Created: 2012-02-24
 * $Id$
 */

package org.hage.util.reflect.predicates;


import com.google.common.base.Predicate;
import org.hage.util.reflect.Methods;

import java.lang.reflect.Method;

import static org.hage.util.reflect.Methods.*;


/**
 * Utility predicates for Methods.
 *
 * @author AGH AgE Team
 * @since 2.6
 */
public final class MethodPredicates {

    private MethodPredicates() {
    }

    /**
     * Returns a predicate checking if methods are boolean getters.
     *
     * @return a predicate
     * @see Methods#isBooleanGetter(Method)
     * @since 2.6
     */
    public static Predicate<Method> forBooleanGetters() {
        return FixedPredicates.BOOLEAN_GETTER;
    }

    /**
     * Returns a predicate checking if methods are getters.
     *
     * @return a predicate
     * @see Methods#isGetter(Method)
     * @since 2.6
     */
    public static Predicate<Method> forGetters() {
        return FixedPredicates.GETTER;
    }

    /**
     * Returns a predicate checking if methods are setters.
     *
     * @return a predicate
     * @see Methods#isSetter(Method)
     * @since 2.6
     */
    public static Predicate<Method> forSetters() {
        return FixedPredicates.SETTER;
    }

    /**
     * Returns a predicate checking if methods are overridden by a given child method.
     *
     * @param child the child method
     * @return a predicate
     * @see Methods#isOverridenBy(Method, Method)
     * @since 2.6
     */
    public static Predicate<Method> overriddenBy(final Method child) {
        return parent -> Methods.isOverridenBy(parent, child);
    }

    /**
     * Stateless predicates.
     *
     * @author AGH AgE Team
     */
    private enum FixedPredicates implements Predicate<Method> {
        BOOLEAN_GETTER {
            @Override
            public boolean apply(final Method input) {
                return isBooleanGetter(input);
            }
        },
        GETTER {
            @Override
            public boolean apply(final Method input) {
                return isGetter(input);
            }
        },
        SETTER {
            @Override
            public boolean apply(final Method input) {
                return isSetter(input);
            }
        }
    }
}
