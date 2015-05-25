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
package org.jage.property.monitors;

import static org.junit.Assert.assertEquals;

import org.jage.property.testHelpers.PropertyMonitorStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PropertyMonitorsSetTest {
	
	@Mock
	IPropertyMonitorRule ruleMock1;
	
	@Mock
	IPropertyMonitorRule ruleMock2;

	@Test
	public void testMonitorsNotification() {
		when(ruleMock1.isActive(anyObject(), anyObject())).thenReturn(true);
		when(ruleMock2.isActive(anyObject(), anyObject())).thenReturn(true);
		when(ruleMock1.isActive(anyObject(), anyObject())).thenReturn(true);
		
		PropertyMonitorStub monitor1 = new PropertyMonitorStub();
		PropertyMonitorStub monitor2 = new PropertyMonitorStub();

		PropertyMonitorsSet set = new PropertyMonitorsSet();
		set.addMonitor(monitor1, ruleMock1);
		set.addMonitor(monitor2, ruleMock2);
		set.notifyMonitors(null, null);

		set.removeMonitor(monitor2);
		set.notifyMonitors(null, null);

		assertEquals(2, monitor1.getPropertyChangedInvokationCounter());
		assertEquals(1, monitor2.getPropertyChangedInvokationCounter());
	}

	@Test
	public void testNotificationWithFalseRule() {
		when(ruleMock1.isActive(anyObject(), anyObject())).thenReturn(false);
		
		PropertyMonitorStub monitor = new PropertyMonitorStub();

		PropertyMonitorsSet set = new PropertyMonitorsSet();
		set.addMonitor(monitor, ruleMock1);
		set.notifyMonitors(null, null);

		assertEquals(0, monitor.getPropertyChangedInvokationCounter());
	}
}
