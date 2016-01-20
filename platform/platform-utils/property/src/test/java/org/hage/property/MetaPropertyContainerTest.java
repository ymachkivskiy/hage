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
package org.hage.property;


import org.junit.Test;

import static org.junit.Assert.*;


public class MetaPropertyContainerTest {

    @Test
    public void testAddRemoveGetMethods() throws PropertyException {
        MetaProperty metaProperty1 = getMockMetaProperty(int.class);
        MetaProperty metaProperty2 = getMockMetaProperty(String.class);

        MetaPropertiesSet container = new MetaPropertiesSet();
        container.addMetaProperty(metaProperty1);
        container.addMetaProperty(metaProperty2);

        assertEquals(metaProperty1, container.getMetaProperty(metaProperty1.getName()));
        assertEquals(metaProperty2, container.getMetaProperty(metaProperty2.getName()));

        container.removeMetaProperty(metaProperty1);
        assertEquals(metaProperty2, container.getMetaProperty(metaProperty2.getName()));
        assertNull(container.getMetaProperty(metaProperty1.getName()));
    }

    private MetaProperty getMockMetaProperty(Class<?> propertyClass) throws PropertyException {
        return new MetaProperty(propertyClass.getName(), propertyClass, false, false);
    }

    @Test
    public void testIterator() throws PropertyException {
        MetaProperty metaProperty1 = getMockMetaProperty(Object.class);
        MetaProperty metaProperty2 = getMockMetaProperty(Float.class);

        MetaPropertiesSet container = new MetaPropertiesSet();
        container.addMetaProperty(metaProperty1);
        container.addMetaProperty(metaProperty2);

        assertContains(container, metaProperty1);
        assertContains(container, metaProperty2);
    }

    private void assertContains(Iterable<MetaProperty> metaProperties, MetaProperty metaProperty) {
        for(MetaProperty existingMetaProperty : metaProperties) {
            if(existingMetaProperty == metaProperty) {
                return;
            }
        }
        fail();
    }
}
