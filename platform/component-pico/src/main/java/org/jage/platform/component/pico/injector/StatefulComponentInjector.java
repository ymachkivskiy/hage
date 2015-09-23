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
 * Created: 2010-09-14
 * $Id$
 */

package org.jage.platform.component.pico.injector;


import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;


/**
 * This class provides an injector that handles the lifecycle of stateful components.
 *
 * @param <T> the type of components this injector can inject into
 * @author AGH AgE Team
 */
public final class StatefulComponentInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an StatefulComponentInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public StatefulComponentInjector(final ComponentDefinition definition) {
        super(definition);
    }

    @Override
    public void doInject(final PicoContainer provider, final T instance) throws PicoCompositionException {
        if(instance instanceof IStatefulComponent) {
            try {
                ((IStatefulComponent) instance).init();
            } catch(final ComponentException e) {
                throw new PicoCompositionException("Exception happenned during component initialization", e);
            }
        }
    }

    @Override
    public String getDescriptor() {
        return "StatefulComponent Injector";
    }
}
