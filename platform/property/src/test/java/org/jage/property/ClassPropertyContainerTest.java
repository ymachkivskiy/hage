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

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jage.event.ObjectChangedEvent;
import org.jage.monitor.IChangesNotifierMonitor;
import org.jage.property.functions.CountFunction;
import org.jage.property.monitors.IPropertyMonitorRule;
import org.jage.property.testHelpers.ChangesNotifierStub;
import org.jage.property.testHelpers.ClassWithProperties;
import org.jage.property.testHelpers.InnerClassWithProperties;
import org.jage.property.testHelpers.PropertyMonitorStub;
import org.jage.property.testHelpers.SubclassWithProperties;
import org.junit.Test;

public class ClassPropertyContainerTest extends ClassPropertyContainer {

	@Test
	public void testGetSetValue() throws Exception {
		SubclassWithProperties container = new SubclassWithProperties();

		container.setIntProperty(0);
		assertEquals(0, container.getProperty("intProperty").getValue());
		container.getProperty("intProperty").setValue(5);
		assertEquals(5, container.getProperty("intProperty").getValue());

		container.setStringProperty("a");
		assertEquals("a", container.getProperty("stringProperty").getValue());
		container.getProperty("stringProperty").setValue("V");
		assertEquals("V", container.getProperty("stringProperty").getValue());

		container.getProperty("floatProperty").setValue(1.0f);
		assertEquals(1.0f, ((Float) container.getProperty("floatProperty")
				.getValue()).floatValue(), 0.0f);
	}

	@Test
	public void testGetMetaProperty() throws Exception {
		ClassWithProperties container = new ClassWithProperties();
		MetaProperty intMetaProperty = container.getProperty("intProperty")
				.getMetaProperty();
		assertTrue(int.class.equals(intMetaProperty.getPropertyClass()));
		assertTrue(intMetaProperty.isWriteable());
		assertTrue(intMetaProperty.isMonitorable());
		assertEquals("intProperty", intMetaProperty.getName());

		MetaProperty objectMetaProperty = container.getProperty(
				"objectProperty").getMetaProperty();
		assertTrue(Object.class.equals(objectMetaProperty.getPropertyClass()));
		assertTrue(objectMetaProperty.isWriteable());
		assertTrue(objectMetaProperty.isMonitorable());
		assertEquals("objectProperty", objectMetaProperty.getName());
	}

	@Test
	public void testMonitors() throws Exception {
		ClassWithProperties container = new ClassWithProperties();
		PropertyMonitorStub monitor1 = new PropertyMonitorStub();

		IPropertyMonitorRule ruleMock1 = mock(IPropertyMonitorRule.class);

		when(ruleMock1.isActive(anyObject(), anyObject())).thenReturn(true, true);

		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		container.addPropertyMonitor("intProperty", monitor1, ruleMock1);
		container.addPropertyMonitor("intProperty", monitor2);
		container.getProperty("intProperty").setValue(5);
		container.removePropertyMonitor("intProperty", monitor2);
		container.getProperty("intProperty").setValue(6);

		// Validating stubs
		assertEquals(2, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(1, monitor2.getPropertyChangedInvokationCounter());
	}

	@Test
	public void testNotifyMonitorsForChangedProperties() throws Exception {

		SampleClassPropertyContainer container = new SampleClassPropertyContainer();
		PropertyMonitorStub monitor = new PropertyMonitorStub();
		
		IPropertyMonitorRule ruleMock = mock(IPropertyMonitorRule.class);

		when(ruleMock.isActive(anyObject(), anyObject())).thenReturn(true, true);
		
		container.addPropertyMonitor("intProperty", monitor, ruleMock);
		container.addPropertyMonitor("stringProperty", monitor, ruleMock);
		
		container.setIntProperty(15);
		container.setStringProperty("newString");
		
		container.notifyMonitorsForChangedProperties();
		
		assertEquals(2, monitor.getPropertyChangedInvokationCounter());
	}

	@Test
	public void testNotifyAboutChanges() throws Exception {
		ClassWithProperties container = new ClassWithProperties();

		PropertyMonitorStub monitor1 = new PropertyMonitorStub();
		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		container.addPropertyMonitor("monitorableObjectProperty", monitor1);
		container.addPropertyMonitor("monitorableStringProperty", monitor2);
		container.setMonitorableObjectPropertyValue(new Object());
		container.invokeNotifyMonitorsForChangedProperties();

		assertEquals(1, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(0, monitor2.getPropertyChangedInvokationCounter());
	}

	/**
	 * Scenario: property informs that it has been changed. Property container
	 * implements IChangesNotifier, and should inform it's monitors about the
	 * change.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testChangesNotification() throws Exception {
		final ClassWithProperties container = new ClassWithProperties();

		IChangesNotifierMonitor monitorMock = mock(IChangesNotifierMonitor.class);

		monitorMock.objectChanged(container, mock(ObjectChangedEvent.class));
		verify(monitorMock).objectChanged(same(container), isA(ObjectChangedEvent.class));

		container.addMonitor(monitorMock);
		container.getProperty("intProperty").setValue(3);

	}

	/**
	 * Scenario: accessing and monitoring complex property. 1. Subproperty is
	 * accessed using complex path. 2. Monitor is attached to the subproperty.
	 * 3. Monitor is attached to the parent property of the subproperty.
	 */
	@Test
	public void testComplexProperty() throws Exception {
		ClassWithProperties container = new ClassWithProperties();

		// Retreiving value of the property.
		Property subProperty = container
				.getProperty("complexProperty.stringProperty");
		assertNotNull(subProperty);

		// Attaching monitor to the property.
		PropertyMonitorStub monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexProperty.stringProperty", monitor);
		subProperty.setValue(subProperty.getValue() + " - new value");
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexProperty.stringProperty",
				monitor);

		// Attaching monitor only to "complexProperty"
		monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexProperty", monitor);
		subProperty.setValue("");
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexProperty", monitor);

		// The monitor should not get the message now.
		subProperty.setValue("newValue");
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
	}

	/**
	 * Scenario: accesing and monitoring complex property that uses arrays. 1.
	 * Subproperty is accessed using complex path with with array indices. 2.
	 * Monitor is attached to the subproperty. 3. Monitor is attached to the
	 * parent property of the subproperty.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testComplexArrayProperty() throws Exception {
		ClassWithProperties container = new ClassWithProperties();

		// Retreiving value of complex property.
		Property subProperty = container
				.getProperty("complexArrayProperty[0].intProperty");
		subProperty.setValue(0);
		assertNotNull(subProperty);

		// Attaching monitor to the property.
		PropertyMonitorStub monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexArrayProperty[0].intProperty",
				monitor);
		subProperty.setValue(1);
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexArrayProperty[0].intProperty",
				monitor);

		// Attaching monitor only to "complexArrayProperty"
		monitor = new PropertyMonitorStub();
		container.addPropertyMonitor("complexArrayProperty", monitor);
		subProperty.setValue(0);
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
		container.removePropertyMonitor("complexArrayProperty", monitor);

		// The monitor should not get the message now.
		subProperty.setValue(1);
		assertEquals(1, monitor.getPropertyChangedInvokationCounter());
	}

	/**
	 * Scenario: adding and removing functions. 1. New function is created. 2.
	 * The function is added to the container. 3. The container should contain
	 * the function. 4. Function should be able to access properties from the
	 * container. 5. Function is removed from the container. 6. The container
	 * shouldn't contain the function.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddingAndRemovingFunctions() throws Exception {
		ClassWithProperties container = new ClassWithProperties();
		CountFunction function = new CountFunction("count",
				"complexArrayProperty[*].stringProperty");
		container.addFunction(function);
		assertSame(function, container.getProperty("count"));
		assertEquals(new Integer(2), function.getValue());
		container.removeFunction(function);
		try {
			container.getProperty("count");
			fail("Function should be removed from the container.");
		} catch (InvalidPropertyPathException ex) {
		}
		;
	}

	/**
	 * Scenario: getting all meta properties, before and after accessing any
	 * property.
	 */
	@Test
	public void testGetMetaProperties() throws Exception {
		ClassWithProperties container = new ClassWithProperties();
		validateMetaProperties(container.getMetaProperties());
		container.getProperties();
		validateMetaProperties(container.getMetaProperties());
	}

	private void validateMetaProperties(MetaPropertiesSet metaProperties) {
		assertEquals(10, metaProperties.size());
		assertEquals(InnerClassWithProperties.class, metaProperties
				.getMetaProperty("complexProperty").getPropertyClass());
		assertEquals(InnerClassWithProperties[].class, metaProperties
				.getMetaProperty("complexArrayProperty").getPropertyClass());
		assertEquals(ChangesNotifierStub.class, metaProperties.getMetaProperty(
				"changesNotifierProperty").getPropertyClass());
		assertEquals(float.class, metaProperties.getMetaProperty(
				"floatProperty").getPropertyClass());
		assertEquals(Object.class, metaProperties.getMetaProperty(
				"objectProperty").getPropertyClass());
		assertEquals(ChangesNotifierStub.class, metaProperties.getMetaProperty(
				"changesNotifierProperty2").getPropertyClass());
		assertEquals(String.class, metaProperties.getMetaProperty(
				"stringProperty").getPropertyClass());
		assertEquals(Object.class, metaProperties.getMetaProperty(
				"monitorableObjectProperty").getPropertyClass());
		assertEquals(String.class, metaProperties.getMetaProperty(
				"monitorableStringProperty").getPropertyClass());
	}
	
    class SampleClassPropertyContainer extends ClassPropertyContainer {
    	
    	@PropertyField(propertyName="intProperty", isMonitorable=true)
    	private int intProperty;
    	
    	@PropertyField(propertyName="int2Property", isMonitorable=true)
    	private int int2Property;
    	
    	@PropertyField(propertyName="intNoMonitorableProperty", isMonitorable=true)
    	private int intNoMonitorableProperty;
    	
    	    	
    	@PropertyField(propertyName="stringProperty", isMonitorable=true)
    	private String stringProperty;

		public int getIntProperty() {
			return intProperty;
		}

		public void setIntProperty(int intProperty) {
			this.intProperty = intProperty;
		}

		public String getStringProperty() {
			return stringProperty;
		}
		

		public int getInt2Property() {
			return int2Property;
		}

		public void setInt2Property(int int2Property) {
			this.int2Property = int2Property;
		}

		public int getIntNoMonitorableProperty() {
			return intNoMonitorableProperty;
		}

		public void setIntNoMonitorableProperty(int intNoMonitorableProperty) {
			this.intNoMonitorableProperty = intNoMonitorableProperty;
		}

		public void setStringProperty(String stringProperty) {
			this.stringProperty = stringProperty;
		}
    }
	
}
