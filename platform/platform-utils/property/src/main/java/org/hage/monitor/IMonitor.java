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


import org.hage.event.AbstractEvent;


/**
 * <p>
 * The base interface for all monitors. It enables a monitored object to inform an external object about its deletion.
 * </p>
 * <p>
 * Note: in order to prevent deadlocks events of monitors can be invoked in a short time after the monitor was
 * unregistered.
 * <p/>
 * <p>
 * Note: monitors should not override equals and hashCode methods.
 * </p>
 *
 * @author AGH AgE Team
 */
public interface IMonitor {

    /**
     * The method executed if the owner of the monitor is being deleted.
     *
     * @param event the event that caused the deletion
     */
    void ownerDeleted(AbstractEvent event);
}
