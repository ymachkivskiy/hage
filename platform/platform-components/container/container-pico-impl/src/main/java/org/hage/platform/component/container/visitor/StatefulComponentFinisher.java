package org.hage.platform.component.container.visitor;
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

import org.hage.platform.component.container.Stateful;
import org.hage.platform.component.container.exception.ComponentException;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentFactory;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Cached;
import org.picocontainer.visitors.AbstractPicoVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PicoVisitor which calls finish on Stateful Components at each level of the hierarchy.
 *
 * @author AGH AgE Team
 */
public class StatefulComponentFinisher extends AbstractPicoVisitor {

    private static final Logger log = LoggerFactory.getLogger(StatefulComponentFinisher.class);

    @Override
    public boolean visitContainer(final PicoContainer pico) {
        for(ComponentAdapter<Stateful> adapter : pico.getComponentAdapters(Stateful.class)) {
            if(adapter.findAdapterOfType(Cached.class) != null) {
                try {
                    adapter.getComponentInstance(pico, ComponentAdapter.NOTHING.class).finish();
                } catch(final ComponentException e) {
                    log.error("Exception during component finish.", e);
                }
            }
        }
        return true;
    }

    @Override
    public void visitComponentAdapter(final ComponentAdapter<?> componentAdapter) {
    }

    @Override
    public void visitComponentFactory(final ComponentFactory componentFactory) {
    }

    @Override
    public void visitParameter(final Parameter parameter) {
    }
}
