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
package org.jage.monitoring.config;


import org.jage.monitoring.handler.HandlerFactoryProvider;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class performs some initial methods on all handlers and observers defined in XML configuration file.
 *
 * @author AGH AgE Team
 */
public class XmlConfig implements IStatefulComponent {

    private List<HandlerFactoryProvider> handlerProviderList;
    private Set<AbstractStatefulObserver> observers;

    public XmlConfig(List<HandlerFactoryProvider> handlerProviderList) {
        this.handlerProviderList = handlerProviderList;
    }

    @Override
    public void init() throws ComponentException {

        observers = new HashSet<>();

        for(HandlerFactoryProvider handlerFactoryProvider : handlerProviderList) {
            handlerFactoryProvider.createAndSubscribeHandlerBasedObservableOnObservers();
            observers.addAll(handlerFactoryProvider.getObservers());
        }
    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }

    /**
     * Returns observers defined in the XML configuration file but only these which are used by handler(s).
     *
     * @return collection of AbstractObserver
     */
    public Set<AbstractStatefulObserver> getUsedObservers() {
        return observers;
    }
}