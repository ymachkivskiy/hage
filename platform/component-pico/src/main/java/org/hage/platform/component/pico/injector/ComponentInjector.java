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
 * Created: 2012-03-01
 * $Id$
 */

package org.hage.platform.component.pico.injector;


import org.hage.platform.component.definition.ComponentDefinition;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This injector is responsible for the lifecycle of component creation and injection.
 * <p>
 * It will first create an instance using constructor injection.
 * <p>
 * Then, it will use setter injection, annotated field and metod injection, and finally some interface injection.
 *
 * @param <T> the type of components this injector can create
 * @author AGH AgE Team
 */
public final class ComponentInjector<T> extends AbstractInjector<T> {

    private static final long serialVersionUID = 1L;

    private final ComponentAdapter<T> instantiator;

    private final List<Injector<T>> injectors;

    /**
     * Creates an ComponentInjector using a given component definition.
     *
     * @param definition the component definition to use
     */
    public ComponentInjector(final ComponentDefinition definition, final ComponentAdapter<T> instantiator,
            final List<Injector<T>> injectors) {
        super(checkNotNull(definition));
        this.instantiator = checkNotNull(instantiator);
        this.injectors = checkNotNull(injectors);
    }

    @Override
    protected void doVerify(final PicoContainer container) {
        instantiator.verify(container);
        for(Injector<T> injector : injectors) {
            injector.verify(container);
        }
    }

    @Override
    public T doCreate(final PicoContainer container) throws PicoCompositionException {
        final T instance = instantiator.getComponentInstance(container, ComponentAdapter.NOTHING.class);
        for(final Injector<T> injector : injectors) {
            injector.decorateComponentInstance(container, ComponentAdapter.NOTHING.class, instance);
        }
        return instance;
    }

    @Override
    protected PicoCompositionException rewrittenException(final String message, final PicoCompositionException e) {
        return e;
    }

    @Override
    public String getDescriptor() {
        return "Component Injector";
    }
}
