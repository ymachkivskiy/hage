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


import org.hage.property.testHelpers.ChangesNotifierStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Tests for the ChangesNotifierArrayMonitoringStrategy class.
 *
 * @author Tomek
 */
public class ChangesNotifierArrayMonitoringStrategyTest extends
                                                        PropertyValueMonitoringStrategyTest {

    private ChangesNotifierArrayMonitoringStrategy _strategy;
    private ChangesNotifierStub[] _changesNotifiers;
    private TestProperty _propertyMock;

    @Before
    public void setUp() {
        _changesNotifiers = new ChangesNotifierStub[]{
                new ChangesNotifierStub(), new ChangesNotifierStub(),
                new ChangesNotifierStub()};
        _propertyMock = new TestProperty(_changesNotifiers);
        _strategy = new ChangesNotifierArrayMonitoringStrategy(_propertyMock);
    }

    @Test
    public void testArrayElementInternalStateChanged() {
        _propertyMock.expectNotifyMonitors(_changesNotifiers);
        _changesNotifiers[1].notifyMonitorsAboutChange();
        assertEquals(1, _propertyMock.getNotifyMonitorsInvokationCount());
    }

    @Test
    public void testArrayElementDeleted() {
        _changesNotifiers[2].notifyMonitorsAboutDeletion();
        _changesNotifiers[2].notifyMonitorsAboutChange();
        assertEquals(0, _propertyMock.getNotifyMonitorsInvokationCount());

        _propertyMock.expectNotifyMonitors(_changesNotifiers);
        _changesNotifiers[1].notifyMonitorsAboutChange();
        assertEquals(1, _propertyMock.getNotifyMonitorsInvokationCount());
    }

    @Test
    public void testPropertyValueChanged() {
        ChangesNotifierStub[] newValue = {new ChangesNotifierStub(),
                new ChangesNotifierStub()};
        _strategy.propertyValueChanged(newValue);
        assertEquals(0, _changesNotifiers[0].getNumberOfAttachedMonitors());
        assertEquals(1, newValue[0].getNumberOfAttachedMonitors());

        _propertyMock.resetNotifyMonitorsInvokationCount();
        _changesNotifiers[1].notifyMonitorsAboutChange();
        assertEquals(0, _propertyMock.getNotifyMonitorsInvokationCount());

        _propertyMock.expectNotifyMonitors(newValue);
        _propertyMock.setValue(newValue);
        newValue[0].notifyMonitorsAboutChange();
        assertEquals(1, _propertyMock.getNotifyMonitorsInvokationCount());
    }
}
