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

package org.hage.monitoring.config;


import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;

import java.util.Date;


/**
 * Default implementation of <code>ComputationInstanceProvider</code> interface.
 * The value of created computation instance is equal to timestamp of calling <code>init</code> method of this component.
 *
 * @author AGH AgE Team
 */
public class DefaultComputationInstanceProvider implements ComputationInstanceProvider, IStatefulComponent {

    private String timestamp;

    @Override
    public void init() throws ComponentException {
        timestamp = new Long(new Date().getTime()).toString();
    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }

    @Override
    public String getComputationInstance() {
        return timestamp;
    }
}
