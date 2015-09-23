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


import org.jage.monitoring.MonitoringException;
import org.jage.monitoring.observer.ObservedData;
import rx.Observable;
import rx.schedulers.Timestamped;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Class invokes method which creates handler.
 *
 * @author AGH AgE Team
 */

public class FactoryMethodInvoker {

    /**
     * Invokes method which name is passed as an argument.
     *
     * @param observables
     * @param observers
     * @param name
     * @param clazz       Class name of the handler.
     * @param method      Method name which would be called.
     */
    public Observable<ObservedData> invokeFactoryMethod(List<Observable<Timestamped<Object>>> observables, String name, String clazz, String method) {
        Observable<ObservedData> result = null;
        try {
            result = (Observable<ObservedData>) Class.forName(clazz)
                    .getMethod(method, List.class, String.class)
                    .invoke(null, observables, name);
            return result;
        } catch(IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            throw new MonitoringException(
                    new StringBuilder().append("Error in handler ").append(name).append(", ")
                            .append(e.getMessage()).append(" ").append(e.getClass()).toString()
                    , e);
        }

    }

}
