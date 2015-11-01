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
 * Created: 2014-03-21
 * $Id$
 */

package org.hage.lifecycle;


import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import org.hage.bus.EventBus;
import org.hage.platform.component.IStatefulComponent;
import org.hage.services.core.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public final class AutoExitHook implements IStatefulComponent {

    private static final Logger log = LoggerFactory.getLogger(AutoExitHook.class);

    @Inject
    private EventBus eventBus;

    @Override
    public void init() {
        eventBus.register(this);
    }

    @Override
    public boolean finish() {
        eventBus.unregister(this);
        return true;
    }

    @Subscribe
    public void onLifecycleStateChangedEvent(@Nonnull final LifecycleStateChangedEvent event) {
        log.debug("LifecycleStateChangedEvent: {}", event);
        if(LifecycleManager.State.STOPPED.equals(event.getNewState())) {
            eventBus.post(new ExitRequestedEvent());
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }
}
