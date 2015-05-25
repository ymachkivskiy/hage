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
package org.jage.monitoring;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.jage.monitoring.config.TypeSafeConfig;
import org.jage.monitoring.config.XmlConfig;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;
import rx.subjects.PublishSubject;
import rx.util.functions.Func1;

import com.google.common.base.Supplier;

/**
 * Monitoring main class, which encapsulates all monitoring specific objects.
 * 
 * @author AGH AgE Team
 */
public class Monitoring implements IStatefulComponent{

	private static final Logger log = LoggerFactory.getLogger(Monitoring.class);
	private XmlConfig xmlConfig;
	private TypeSafeConfig typeSafeConfig;
	
	private static PublishSubject<Long> exitSignal = PublishSubject.create();
	
	public Monitoring(XmlConfig xmlConfig){
		this.xmlConfig = xmlConfig;
	}
	
	public Monitoring(TypeSafeConfig typeSafeLoader){
		this.typeSafeConfig = typeSafeLoader;
	}
	
	public Monitoring(XmlConfig xmlConfig, TypeSafeConfig typeSafeConfig){
		this.xmlConfig = xmlConfig;
		this.typeSafeConfig = typeSafeConfig;
	}

	@Override
	public void init() throws ComponentException {
		Collection<AbstractStatefulObserver> observers = getAllObservers();
		mergeHandlersAndSubscribeOnObservers(observers);
	}

	private void mergeHandlersAndSubscribeOnObservers(Collection<AbstractStatefulObserver> observers) {
		for (AbstractStatefulObserver observer : observers) {
			Observable.merge(observer.getObservables()).subscribe(observer);
		}
	}

	public Collection<AbstractStatefulObserver> getAllObservers(){
		Collection<AbstractStatefulObserver> observers = new HashSet<>();
		if(xmlConfig != null){
			observers.addAll(xmlConfig.getUsedObservers());
		}
		if(typeSafeConfig != null){
			observers.addAll(typeSafeConfig.getUsedObservers());
		}
		return observers;
	}
	
	@Override
	public boolean finish() throws ComponentException {
		stopMonitoring();
		return false;
	}
	
	/**
	 * Creates Observable object which every <code>rate</code> milliseconds provides data from a given supplier.
	 * @param supplier
	 * @param rate
	 * @return Timestamped Observable object.
	 */
	public static Observable<Timestamped<Object>> createObservableFromSupplier(final Supplier<? extends Object> supplier, long rate){
		return createObservableFromSupplier(supplier, rate, Schedulers.computation());
	}
	
	/**
	 * Creates Observable object which every <code>rate</code> milliseconds provides data from a given supplier, using a given scheduler.
	 * @param supplier
	 * @param rate
	 * @param scheduler
	 * @return Timestamped Observable object.
	 */
	public static Observable<Timestamped<Object>> createObservableFromSupplier(final Supplier<? extends Object> supplier, long rate, Scheduler scheduler){		
		return Observable.interval(rate, TimeUnit.MILLISECONDS, scheduler).takeUntil(exitSignal).map(new Func1<Long, Object>() {
			@Override
			public Object call(Long t) {
				return supplier.get();
			}
		}).timestamp();
	}
	
	/**
	 * Stops monitoring.
	 */
	public static void stopMonitoring(){
		exitSignal.onCompleted();
		exitSignal = PublishSubject.create();
		log.info("Exit called by Monitoring");
}
	}