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
 * Created: 2011-09-12
 * $Id$
 */

package org.hage.platform.component.query;


import org.hage.address.agent.AgentAddress;
import org.hage.platform.component.agent.IAgent;
import org.hage.property.IPropertyContainer;

import java.lang.reflect.Method;


/**
 * Common implementations of {@link IValueSelector}.
 *
 * @author AGH AgE Team
 */
public final class ValueSelectors {

    private ValueSelectors() {
        // Empty
    }

    /**
     * Creates a filter that matches a field of the Java Bean with the provided filter. This implementation uses
     * reflection.
     *
     * @param fieldName A field to use.
     * @param <T>       A generic type of the checked object.
     * @param <S>       A generic type of the field to test.
     * @return A new value filter.
     */
    public static <T, S> IValueSelector<T, S> field(final String fieldName) {
        return new IValueSelector<T, S>() {

            @SuppressWarnings("unchecked")
            @Override
            public S selectValue(T object) {
                try {
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = object.getClass().getMethod(methodName, (Class<?>[]) null);
                    S value = (S) method.invoke(object);
                    return value;

                } catch(Exception e) {
                    throw new QueryException(e);
                }
            }

        };
    }

    /**
     * Creates a filter that matches a property of a property container with the provided filter.
     *
     * @param propertyName a name of the property.
     * @param <T>          A generic type of the checked object.
     * @param <S>          A generic type of the property value.
     * @return A new value filter.
     */
    public static <T extends IPropertyContainer, S> IValueSelector<T, S> property(final String propertyName) {
        return new IValueSelector<T, S>() {

            @SuppressWarnings("unchecked")
            @Override
            public S selectValue(T object) {
                try {
                    return (S) object.getProperty(propertyName).getValue();
                } catch(Exception e) {
                    throw new QueryException(e);
                }
            }

        };
    }

    public static <T extends IAgent> IValueSelector<T, AgentAddress> agentAddress() {
        return new IValueSelector<T, AgentAddress>() {

            @SuppressWarnings("unchecked")
            @Override
            public AgentAddress selectValue(T object) {
                return object.getAddress();
            }
        };
    }
}
