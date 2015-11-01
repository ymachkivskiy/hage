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
 * Created: Jun 8, 2010
 * $Id$
 */

package org.hage.platform.component;


import org.hage.platform.component.exception.ComponentException;


/**
 * @author AGH AgE Team
 */
public interface IStatefulComponent {

    /**
     * Called just after the component instance is created and before the instance is returned to the client (note: this
     * method is called only once while creating a component instance).
     *
     * @throws ComponentException when any error during initialization occurs
     */
    void init() throws ComponentException;

    /**
     * Called just before the component instance is unregistered; this is usually performed when the platform is going
     * to shutdown. The method should be used to perform a cleanup, for instance closing network connections, removing
     * temporary files.
     *
     * @return <code>true</code> when the component is finished, <code>false</code> when component is still finishing
     * (it is long running operation)
     * @throws ComponentException when any error during finishing occurs
     */
    boolean finish() throws ComponentException;
}
