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
package org.jage.property;


import org.jage.property.testHelpers.ClassWithProperties;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


public class PropertiesSetTest {

    @Test
    public void testAddRemoveGetIterator() throws Exception {
        Property property1 = getIntProperty();
        Property property2 = getStringProperty();

        PropertiesSet set = new PropertiesSet();
        set.addProperty(property1);
        set.addProperty(property2);
        assertEquals(property1, set.getProperty(property1.getMetaProperty()
                                                        .getName()));
        assertEquals(property2, set.getProperty(property2.getMetaProperty()
                                                        .getName()));
        assertContainsProperty(set, property1);
        assertContainsProperty(set, property2);

        set.removeProperty(property2);
        assertEquals(property1, set.getProperty(property1.getMetaProperty()
                                                        .getName()));
        assertNull(set.getProperty(property2.getMetaProperty().getName()));
    }

    private GetterSetterProperty getIntProperty() throws Exception {
        ClassWithProperties propertiesObject = new ClassWithProperties();
        Method intPropertyGetter = propertiesObject.getIntPropertyGetter();
        Method intPropertySetter = propertiesObject.getIntPropertySetter();
        PropertyGetter intAnnotation = intPropertyGetter.getAnnotation(PropertyGetter.class);
        GetterSetterMetaProperty metaProperty = new GetterSetterMetaProperty(
                intAnnotation.propertyName(), intPropertyGetter, intPropertySetter, intAnnotation.isMonitorable());
        return new GetterSetterProperty(metaProperty, propertiesObject);
    }

    private GetterSetterProperty getStringProperty() throws Exception {
        ClassWithProperties propertiesObject = new ClassWithProperties();
        Method stringPropertyGetter = propertiesObject.getStringPropertyGetter();
        Method stringPropertySetter = propertiesObject.getStringPropertySetter();
        PropertyGetter stringAnnotation = stringPropertyGetter.getAnnotation(PropertyGetter.class);
        GetterSetterMetaProperty metaProperty = new GetterSetterMetaProperty(
                stringAnnotation.propertyName(), stringPropertyGetter, stringPropertySetter, stringAnnotation.isMonitorable());

        return new GetterSetterProperty(metaProperty, propertiesObject);
    }

    private void assertContainsProperty(Iterable<Property> set,
            Property property) {
        for(Property existingProperty : set) {
            if(existingProperty == property) {
                return;
            }
        }
        fail();
    }

    @Test
    public void testGetMetaPropertyContainer() throws Exception {
        Property property1 = getIntProperty();
        Property property2 = getStringProperty();
        MetaProperty metaProperty1 = property1.getMetaProperty();
        MetaProperty metaProperty2 = property2.getMetaProperty();

        PropertiesSet set = new PropertiesSet();
        set.addProperty(property1);
        set.addProperty(property2);

        assertEquals(metaProperty1, set.getMetaPropertiesSet().getMetaProperty(
                metaProperty1.getName()));
        assertEquals(metaProperty2, set.getMetaPropertiesSet().getMetaProperty(
                metaProperty2.getName()));
    }

    @Test
    public void testDuplicateProperty() throws Exception {
        Property property1 = new SimpleProperty(new MetaProperty("a",
                                                                 Integer.class, true, false, false), new Integer(1));
        Property property2 = new SimpleProperty(new MetaProperty("a",
                                                                 String.class, true, false, false), "aaa");
        PropertiesSet set = new PropertiesSet();
        set.addProperty(property1);
        try {
            set.addProperty(property2);
            fail();
        } catch(DuplicatePropertyNameException ex) {
        }
    }
}
