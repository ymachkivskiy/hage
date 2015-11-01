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
 * Created: 2010-06-23
 * $Id$
 */

package org.hage.property;


import org.hage.event.AbstractEvent;
import org.hage.event.ObjectChangedEvent;
import org.hage.monitor.IChangesNotifier;
import org.hage.monitor.IChangesNotifierMonitor;


/**
 * Strategy for IChangesNotifier type. This class is used by Property class.
 *
 * @author AGH AgE Team
 */
public class ChangesNotifierMonitoringStrategy extends PropertyValueMonitoringStrategy {

    private ChangesNotifierMonitor monitor;

    private IChangesNotifier monitoredPropertyValue;

    /**
     * Constructor.
     *
     * @param property property that uses this strategy.
     */
    public ChangesNotifierMonitoringStrategy(Property property) {
        super(property);
        monitoredPropertyValue = (IChangesNotifier) property.getValue();
        attachMonitor();
    }

    private void attachMonitor() {
        if(monitoredPropertyValue != null) {
            monitor = new ChangesNotifierMonitor();
            monitoredPropertyValue.addMonitor(monitor);
        }
    }

    /**
     * Informs the strategy that property value has been changed.
     *
     * @param newValue new property value
     */
    @Override
    public void propertyValueChanged(Object newValue) {
        if(newValue != getOldValue()) {
            detachMonitor();
            monitoredPropertyValue = (IChangesNotifier) newValue;
            attachMonitor();
            setOldValue(newValue);
        }
    }

    private void detachMonitor() {
        if(monitoredPropertyValue != null) {
            monitoredPropertyValue.removeMonitor(monitor);
            monitor = null;
        }
    }

    private void monitoredObjectChanged() {
        getProperty().notifyMonitors(monitoredPropertyValue);
    }

    private void monitoredObjectDeleted() {
        monitoredPropertyValue.removeMonitor(monitor);
        monitor = null;
    }

    /**
     * A private implementation of {@link IChangesNotifierMonitor}.
     *
     * @author AGH AgE Team
     */
    private class ChangesNotifierMonitor implements IChangesNotifierMonitor {

        public void objectChanged(Object sender, ObjectChangedEvent event) {
            monitoredObjectChanged();
        }

        public void ownerDeleted(AbstractEvent event) {
            monitoredObjectDeleted();
        }
    }
}
