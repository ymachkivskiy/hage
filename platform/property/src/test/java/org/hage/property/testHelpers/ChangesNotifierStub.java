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
package org.hage.property.testHelpers;


import org.hage.event.ObjectChangedEvent;
import org.hage.monitor.IChangesNotifier;
import org.hage.monitor.IChangesNotifierMonitor;

import java.util.ArrayList;


public class ChangesNotifierStub implements IChangesNotifier {

    private ArrayList<IChangesNotifierMonitor> _monitors;

    public ChangesNotifierStub() {
        _monitors = new ArrayList<IChangesNotifierMonitor>();
    }

    public int getNumberOfAttachedMonitors() {
        return _monitors.size();
    }

    public void notifyMonitorsAboutChange() {
        for(IChangesNotifierMonitor monitor : _monitors) {
            monitor.objectChanged(this, new ObjectChangedEvent(this));
        }
    }

    public void notifyMonitorsAboutDeletion() {
        ArrayList<IChangesNotifierMonitor> monitorsCopy = new ArrayList<IChangesNotifierMonitor>(_monitors);
        for(IChangesNotifierMonitor monitor : monitorsCopy) {
            monitor.ownerDeleted(null);
        }
    }

    public void addMonitor(IChangesNotifierMonitor monitor) {
        _monitors.add(monitor);
    }

    public void removeMonitor(IChangesNotifierMonitor monitor) {
        _monitors.remove(monitor);
    }
}