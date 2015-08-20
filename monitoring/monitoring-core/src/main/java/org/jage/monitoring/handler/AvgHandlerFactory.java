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

import java.math.BigInteger;
import java.util.List;

import org.jage.monitoring.observer.ObservedData;

import rx.Observable;
import rx.schedulers.Timestamped;
import rx.functions.FuncN;

/**
 * Handler computes the average from given Observables. 
 * 
 * @author AGH AgE Team
 *
 */
public class AvgHandlerFactory {

	private static Avg avg = new Avg();
	
	/** 
	 * Method creates Observable object which emits average value from the passed list of Observable objects.
	 * The new Observable object is subscribed to Observers object passed in the list argument.
	 * Time of new average value, is assigned new time which is also an average of time of each values. 
	 * 
	 * @param observables
	 * @param observers
	 * @param name The name of the handler. 
	 */
	public static Observable<ObservedData> create(List<Observable<Timestamped<Object>>> observables, final String name) {
		Observable<Timestamped<Object>> zipped = Observable.zip(observables, avg);
		Observable<ObservedData> mapped = Handlers.toObservedData(zipped, name);
		return mapped;
	}

	static class Avg implements FuncN<Timestamped<Object>>{
		@Override
		public Timestamped<Object> call(Object... args) {
			double val = 0;
			BigInteger time = BigInteger.ZERO;
			for (Object o : args) {
				Timestamped<Object> to = (Timestamped<Object>) o;
				val += (((Number)to.getValue()).doubleValue());
				time = time.add(BigInteger.valueOf(to.getTimestampMillis()));
			}
			return new Timestamped<Object>(
					time.divide(BigInteger.valueOf(args.length)).longValue(),
					val /= args.length
					);
		}
	}
}