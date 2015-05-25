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
package org.jage.monitoring.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.jage.monitoring.observer.ObservedData;

import rx.Observable;
import rx.schedulers.Timestamped;
import rx.functions.Func1;

/**
 * Common code which maps Observables into ObservedData.
 * 
 * @author AGH AgE Team
 *
 */
public class Handlers {

	/**
	 * Performs basic mapping the data from Observable into ObservedData.
	 * @param observable
	 * @param name The name of the handler. 
	 * @return Observable<ObservedData>
	 */
	public static Observable<ObservedData> toObservedData(Observable<Timestamped<Object>> observable, final String name){
		return observable.map(new Func1<Timestamped<Object>, ObservedData>() {
			@Override
			public ObservedData call(Timestamped<Object> t1) {
				return new ObservedData(name, t1.getTimestampMillis(), t1.getValue());
			}
		});
	}
	
	/**
	 * Performs basic mapping the data from collection of Observable into collection of ObservedData.
	 * @param observables
	 * @param name The name of the handler. 
	 * @return Collection<Observable<ObservedData>>
	 */
	public static Collection<Observable<ObservedData>> toObservedData(Collection<Observable<Timestamped<Object>>> observables, final String name) {
		Collection<Observable<ObservedData>> mappedObservables = new ArrayList<>();
		for (Observable<Timestamped<Object>> observable : observables) {
			mappedObservables.add(observable.map(new Func1<Timestamped<Object>, ObservedData>() {
				@Override
				public ObservedData call(Timestamped<Object> t1) {
					return new ObservedData(name, t1
							.getTimestampMillis(), t1.getValue());
				}
			}));
		}
		return mappedObservables;
	}
}
