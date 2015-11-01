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
package org.hage.monitoring.handler;


import org.hage.monitoring.observer.ObservedData;
import rx.Observable;
import rx.schedulers.Timestamped;

import java.util.List;


/**
 * Basic handler. Does not perform any data transformation.
 *
 * @author AGH AgE Team
 */
public class BasicHandlerFactory {

    /**
     * Method creates one merged Observable object from the passed list of Observables.
     *
     * @param observables
     * @param observers
     * @param name        The name of handler
     */
    public static Observable<ObservedData> create(List<Observable<Timestamped<Object>>> observables, final String name) {
        return Observable.merge(Handlers.toObservedData(observables, name));
    }
}