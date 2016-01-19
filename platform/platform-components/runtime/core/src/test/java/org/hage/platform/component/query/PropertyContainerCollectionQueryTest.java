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
 * Created: 2011-09-19
 * $Id$
 */

package org.hage.platform.component.query;


import org.hage.property.ClassPropertyContainer;
import org.hage.property.PropertyField;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Tests for the {@link PropertyContainerCollectionQuery} class.
 *
 * @author AGH AgE Team
 */
public class PropertyContainerCollectionQueryTest {

    private List<ClassPropertyContainer> target;

    private PropertyContainerCollectionQuery<ClassPropertyContainer, ClassPropertyContainer> query;

    @Before
    public void setUp() {
        query = new PropertyContainerCollectionQuery<ClassPropertyContainer, ClassPropertyContainer>(
                ClassPropertyContainer.class, Collection.class, ArrayList.class);

        target = new ArrayList<ClassPropertyContainer>();
        target.add(new ClassPropertyContainer() {

            @SuppressWarnings("unused")
            @PropertyField(propertyName = "lorem")
            private String lorem = "ipsum";
        });
        target.add(new ClassPropertyContainer() {

            @SuppressWarnings("unused")
            @PropertyField(propertyName = "lorem")
            private String lorem = "dolor";
        });
        target.add(new ClassPropertyContainer() {

            @SuppressWarnings("unused")
            @PropertyField(propertyName = "lorem")
            private String lorem = "sit";
        });
    }

    /**
     * Tests the specialised {@link PropertyContainerCollectionQuery#matching(String, IValueFilter)} method for querying
     * property values.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMatchingForProperty() {
        IValueFilter<String> valueFilterMock = mock(IValueFilter.class);
        when(valueFilterMock.matches("ipsum")).thenReturn(true);
        query.matching("lorem", valueFilterMock);

        Collection<ClassPropertyContainer> result = query.execute(target);
        assertEquals(target.subList(0, 1), result);
    }

}
