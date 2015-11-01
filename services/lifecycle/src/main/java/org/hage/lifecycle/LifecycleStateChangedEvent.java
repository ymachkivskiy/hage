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
 * Created: 2013-09-14
 * $Id$
 */

package org.hage.lifecycle;


import org.hage.annotation.FieldsAreNonnullByDefault;
import org.hage.annotation.ReturnValuesAreNonnullByDefault;
import org.hage.bus.AgeEvent;
import org.hage.platform.fsm.StateChangedEvent;
import org.hage.services.core.LifecycleManager;

import javax.annotation.concurrent.Immutable;


@Immutable
@FieldsAreNonnullByDefault
@ReturnValuesAreNonnullByDefault
public class LifecycleStateChangedEvent extends StateChangedEvent<LifecycleManager.State, LifecycleManager.Event>
        implements AgeEvent {

    /**
     * Creates a new event.
     *
     * @param previousState a previous state.
     * @param event         an event that caused the transition.
     * @param newState      a new state.
     */
    private LifecycleStateChangedEvent(final LifecycleManager.State previousState,
            final LifecycleManager.Event event, final LifecycleManager.State newState) {
        super(previousState, event, newState);
    }

    /**
     * Creates a new event.
     *
     * @param previousState a previous state.
     * @param event         an event that caused the transition.
     * @param newState      a new state.
     * @return a new event.
     */
    public static LifecycleStateChangedEvent create(final LifecycleManager.State previousState,
            final LifecycleManager.Event event, final LifecycleManager.State newState) {
        return new LifecycleStateChangedEvent(previousState, event, newState);
    }
}
