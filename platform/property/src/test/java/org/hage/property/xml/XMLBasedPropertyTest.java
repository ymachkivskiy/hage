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
package org.hage.property.xml;


import org.hage.property.InvalidPropertyOperationException;
import org.hage.property.MetaProperty;
import org.hage.property.Property;
import org.hage.property.PropertyException;
import org.hage.property.xml.testHelpers.AdvancedExampleComponent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class XMLBasedPropertyTest {

    private AdvancedExampleComponent component;
    private XMLBasedPropertyContainer _propertiesObject;

    @Before
    public void setUp() throws Exception {
        component = new AdvancedExampleComponent();
        _propertiesObject = new XMLBasedPropertyContainer(XMLBasedPropertyProvider.getInstance().getMetaPropertiesSet(
                component.getClass()), component);
    }

    @Test
    public void testGetSetValue() throws PropertyException {
        component.setIntProperty(0);
        Property property = getIntProperty();
        assertEquals(0, property.getValue());
        property.setValue(1);
        assertEquals(1, property.getValue());
        assertEquals(1, component.getIntProperty());
    }

    private XMLBasedGetterSetterProperty getIntProperty() throws PropertyException {
        XMLBasedGetterSetterMetaProperty metaProperty = new XMLBasedGetterSetterMetaProperty("intProperty", int.class,
                                                                                             component.getIntPropertyGetter(), component.getIntPropertySetter());
        return new XMLBasedGetterSetterProperty(metaProperty, component);
    }

    @Test
    public void testReadOnlyProperty() throws PropertyException {
        Property property = getReadonlyIntProperty();
        try {
            property.setValue(1);
            fail();
        } catch(InvalidPropertyOperationException ex) {
        }
    }

    private XMLBasedGetterSetterProperty getReadonlyIntProperty() throws PropertyException {
        XMLBasedGetterSetterMetaProperty metaProperty = new XMLBasedGetterSetterMetaProperty("intProperty", int.class,
                                                                                             component.getIntPropertyGetter(), component.getIntPropertySetter());
        return new XMLBasedGetterSetterProperty(metaProperty, _propertiesObject);
    }

    @Test
    public void testGetMetaProperty() throws PropertyException {
        Property property = getIntProperty();
        MetaProperty metaProperty = property.getMetaProperty();
        assertTrue(int.class.equals(metaProperty.getPropertyClass()));
        // intProperty is served by XMLGetterSetterMetaProperty, which doesn't support monitoring
        // assertTrue(metaProperty.isMonitorable());
        assertTrue(metaProperty.isWriteable());
    }

	/*
     * _intProperty is served by XMLGetterSetterMetaProperty, which doesn't support monitoring
	 * 
	 * @Test public void testPropertyMonitors() throws Exception {
	 * 
	 * IPropertyMonitorRule ruleMock1 = createMock(IPropertyMonitorRule.class); IPropertyMonitorRule ruleMock2 =
	 * createMock(IPropertyMonitorRule.class);
	 * 
	 * expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).once();
	 * expect(ruleMock2.isActive(anyObject(), anyObject())).andReturn(true).once();
	 * expect(ruleMock1.isActive(anyObject(), anyObject())).andReturn(true).once();
	 * 
	 * replay(ruleMock1, ruleMock2);
	 * 
	 * PropertyMonitorStub monitor1 = new PropertyMonitorStub(); PropertyMonitorStub monitor2 = new
	 * PropertyMonitorStub();
	 * 
	 * Property property = getIntProperty(); property.addMonitor(monitor1, ruleMock1); property.addMonitor(monitor2,
	 * ruleMock2); property.notifyMonitors(null);
	 * 
	 * property.removeMonitor(monitor2); property.notifyMonitors(null);
	 * 
	 * verify(ruleMock1, ruleMock2); assertEquals(2, monitor1.getPropertyChangedInvokationCounter()); assertEquals(1,
	 * monitor2.getPropertyChangedInvokationCounter()); }
	 */

    /**
     * Scenario: property value is object that implements IChangesNotifier interface. It changes its internal state and
     * informs monitors about it. The property itself should inform its monitors about change.
     *
     * @throws Exception
     */
    /*
     * _changesNotifierProperty2 is served by XMLGetterSetterMetaProperty, which doesn't support monitoring
	 * 
	 * @Test public void testChangesNotifierProperty() throws Exception { PropertyMonitorStub monitor = new
	 * PropertyMonitorStub();
	 * 
	 * Property property = _propertiesObject.getProperty("changesNotifierProperty2"); property.addMonitor(monitor, new
	 * DefaultPropertyMonitorRule());
	 * 
	 * ChangesNotifierStub notifier = (ChangesNotifierStub) property.getValue(); notifier.notifyMonitorsAboutChange();
	 * 
	 * assertEquals(1, monitor.getPropertyChangedInvokationCounter()); }
	 */
}
