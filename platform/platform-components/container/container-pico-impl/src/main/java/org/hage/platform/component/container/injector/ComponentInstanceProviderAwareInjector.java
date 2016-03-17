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

package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.*;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This class provides an injector that is able to inject an instance provider into ComponentInstanceProvider-aware
 * components.
 *
 * @param <T> the type of components this injector can inject into
 * @author AGH AgE Team
 */
public final class ComponentInstanceProviderAwareInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private final ComponentDefinition definition;

    /**
     * Creates an ComponentInstanceProviderAwareInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public ComponentInstanceProviderAwareInjector(final ComponentDefinition definition) {
        super(definition);
        this.definition = checkNotNull(definition);
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        if(!(container instanceof PicoMutableInstanceContainer)) {
            throw new PicoCompositionException("The container needs to implement IPicoComponentInstanceProvider");
        }
    }

    @Override
    public void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
        final PicoMutableInstanceContainer provider = (PicoMutableInstanceContainer) container;

        if(instance instanceof InstanceContainerAware) {
            ((InstanceContainerAware) instance).setInstanceProvider(provider);
        }
        if(instance instanceof MutableInstanceContainerAware) {
            ((MutableInstanceContainerAware) instance).setMutableInstanceContainer(provider);
        }
        if(instance instanceof SelfAwareInstanceContainerAware) {
            ((SelfAwareInstanceContainerAware) instance)
                    .setSelfAwareInstanceContainer(new DefaultSelfAwareInstanceContainer(provider, definition));
        }
    }

    @Override
    public String getDescriptor() {
        return "ComponentInstanceProviderAware Injector";
    }
}
