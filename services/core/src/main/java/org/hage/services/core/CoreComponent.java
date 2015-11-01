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
 * Created: 2009-05-18
 * $Id$
 */

package org.hage.services.core;


import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;


/**
 * An interface which represents a core component responsible for the computation processing.
 * <p>
 * A computation can be in several states:
 * <ol>
 * <li>initialized - after all components have been created,
 * <li>started,
 * <li>paused,
 * <li>stopped - after it has been finished.
 * </ol>
 *
 * @author AGH AgE Team
 */
public interface CoreComponent extends IStatefulComponent {

    /**
     * Starts the computation. This method is executed after the component initialization and establishing initial
     * values of its properties.
     *
     * @throws ComponentException occurs when the component cannot be started
     */
    void start() throws ComponentException;

    /**
     * Pauses the computation.
     */
    void pause();

    /**
     * Resumes the computation.
     */
    void resume();

    /**
     * Stops the computation.
     * <p>
     * Should be implemented asynchronously.
     */
    void stop();

    /**
     * Destroys the current configuration.
     */
    void teardownConfiguration();
}
