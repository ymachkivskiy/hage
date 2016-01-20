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

package org.hage.monitor;


import org.hage.event.ObjectChangedEvent;


/**
 * An interface for listeners which are notified by IChangesNotifier objects.
 *
 * @author AGH AgE Team
 */
public interface IChangesNotifierMonitor extends IMonitor {

    /**
     * Called when monitored object is changed.
     *
     * @param sender the object which generates the event
     * @param event  the event
     */
    void objectChanged(Object sender, ObjectChangedEvent event);

}
