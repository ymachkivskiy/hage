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
 * Created: 2012-08-21
 * $Id$
 */

package org.jage.lifecycle;


import org.jage.annotation.ReturnValuesAreNonnullByDefault;

import javax.annotation.concurrent.ThreadSafe;


/**
 * Utilities for working with lifecycle messages.
 *
 * @author AGH AgE Team
 */
@ReturnValuesAreNonnullByDefault
@ThreadSafe
public final class LifecycleMessages {

    private LifecycleMessages() {
        // Empty
    }

    /**
     * Creates an instance of the <strong>START</strong> {@link LifecycleMessage}.
     *
     * @return a new START LifecycleMessage.
     */
    public static LifecycleMessage createStart() {
        return LifecycleMessage.create(LifecycleMessage.LifecycleCommand.START);
    }

    /**
     * Creates an instance of the <strong>EXIT</strong> {@link LifecycleMessage}.
     *
     * @return a new EXIT LifecycleMessage.
     */
    public static LifecycleMessage createExit() {
        return LifecycleMessage.create(LifecycleMessage.LifecycleCommand.EXIT);
    }
}
