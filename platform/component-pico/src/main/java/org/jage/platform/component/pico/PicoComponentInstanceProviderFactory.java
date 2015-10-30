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
 * Created: 2008-10-08
 * $Id$
 */

package org.jage.platform.component.pico;


import org.jage.platform.component.definition.IComponentDefinition;

import java.util.Collection;


/**
 * Creates PicoContainer hierarchy using list of IComponentDefinition objects.
 *
 * @author AGH AgE Team
 */
public abstract class PicoComponentInstanceProviderFactory {

    /**
     * Creates PicoContainer hierarchy for a given list of {@link IComponentDefinition}'s.
     *
     * @param defs list of {@link IComponentDefinition}'s
     * @return a root PicoContainer
     */
    public static IPicoComponentInstanceProvider createInstanceProvider(Collection<IComponentDefinition> defs) {
        IPicoComponentInstanceProvider pico = new PicoComponentInstanceProvider();

        for(IComponentDefinition definition : defs) {
            pico.addComponent(definition);
        }
        return pico;
    }

    /**
     * Creates a new empty PicoContainer.
     *
     * @return A new and empty PicoContainer.
     */
    public static IPicoComponentInstanceProvider createInstanceProvider() {
        return new PicoComponentInstanceProvider();
    }

}
