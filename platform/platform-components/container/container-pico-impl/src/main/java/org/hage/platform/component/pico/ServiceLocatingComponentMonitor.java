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
 * Created: 2011-04-16
 * $Id$
 */

package org.hage.platform.component.pico;


import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.monitors.AbstractComponentMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * This is a component monitor that monitors failed attempts to locate a component in the container. When this happens
 * it try to locate this component in components implementing IImplementationProvider interface.
 * <p>
 * Note about performance: DefaultPicoContainer behaves ridiculously and when it retrieves an instance it tries to
 * "pull" it down to the lowest container. That is not a problem, but when doing this, it goes to the top and calls the
 * monitor as many times as there is levels. (It would be worth to override their implementation in future.)
 * <p>
 * This class should not be used unless AGE-137 is fixed otherwise.
 *
 * @author AGH AgE Team
 */
@Deprecated()
public class ServiceLocatingComponentMonitor extends AbstractComponentMonitor {

    private static final long serialVersionUID = -8387031118893242216L;

    private static Logger log = LoggerFactory.getLogger(ServiceLocatingComponentMonitor.class);

    /**
     * Handles the noComponentFound event.
     * <p>
     * {@inheritDoc}
     *
     * @param container    The container that caused the event.
     * @param componentKey A key to look up. This implementation accepts only String or Class instances as keys.
     * @return The found component or <code>null</code> if no component was found or <code>componentKey</code> was not a
     * string or Class instance.
     * @see org.picocontainer.monitors.AbstractComponentMonitor#noComponentFound(org.picocontainer.MutablePicoContainer,
     * java.lang.Object)
     */
    @Override
    public Object noComponentFound(final MutablePicoContainer container, final Object componentKey) {
        log.debug("No component found: " + componentKey + " in " + container);

        if(!(componentKey instanceof String) && !(componentKey instanceof Class<?>)) {
            return null;
        }

        List<IComponentInstanceProvider> providers = container.getComponents(IComponentInstanceProvider.class);

        log.trace("Providers found: " + providers);

        for(IComponentInstanceProvider provider : providers) {
            Object implementation = null;
            if(componentKey instanceof String) {
                implementation = provider.getInstance((String) componentKey);
            } else if(componentKey instanceof Class) {
                implementation = provider.getInstance((Class<?>) componentKey);
            }
            if(implementation != null) {
                log.trace("Implementation found: " + implementation);
                return implementation;
            }
        }

        return null;
    }

}
