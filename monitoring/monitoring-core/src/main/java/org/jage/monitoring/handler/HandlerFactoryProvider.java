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
import java.util.List;

import org.jage.monitoring.config.FactoryMethodInvoker;
import org.jage.monitoring.observable.ObservableProvider;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.monitoring.observer.ObservedData;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Timestamped;

/**
 * Class created to aggregate information about handler instance in XML configuration file.
 * 
 * @author AGH AgE Team
 *
 */
public class HandlerFactoryProvider {

	private String name;
	private List<ObservableProvider> observableProviders;
	private List<AbstractStatefulObserver> observers;
	private String clazz;
	private String method;
	private FactoryMethodInvoker factoryMethodInvoker;
	
	public HandlerFactoryProvider(String name, List<ObservableProvider> observableProviders,
			List<AbstractStatefulObserver> observers, String clazz,
			String method) {
		this.name = name;
		this.observableProviders = observableProviders;
		this.observers = observers;
		this.clazz = clazz;
		this.method = method;
		factoryMethodInvoker = new FactoryMethodInvoker();
	}
	
	private List<Observable<Timestamped<Object>>> createObservableListFromObservableProviders(List<ObservableProvider> observableProviders){
		List<Observable<Timestamped<Object>>> observables = new ArrayList<>();
		for (ObservableProvider observableProvider : observableProviders) {
			observables.add(observableProvider.provideObservable());
		}
		return observables;
	}
	
	private void assignMergedObservableToObservers(Observable<ObservedData> mergedObservable) {
		for (AbstractStatefulObserver observer : observers) {
			observer.addObservable(mergedObservable);
		}
	}
	
	public void createAndSubscribeHandlerBasedObservableOnObservers(){
		
		Observable<ObservedData> mergedObservable = factoryMethodInvoker.invokeFactoryMethod(
				createObservableListFromObservableProviders(observableProviders),
				name, clazz, method);
		assignMergedObservableToObservers(mergedObservable);
	}
	
	public void setFactoryMethodInvoker(FactoryMethodInvoker factoryMethodInvoker) {
		this.factoryMethodInvoker = factoryMethodInvoker;
	}

	public List<AbstractStatefulObserver> getObservers() {
		return observers;
	}
}