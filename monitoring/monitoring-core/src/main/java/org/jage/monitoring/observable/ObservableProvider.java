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
package org.jage.monitoring.observable;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.jage.monitoring.Monitoring;
import org.jage.monitoring.config.RxSchedulerProvider;

import rx.Observable;
import rx.schedulers.Timestamped;

import com.google.common.base.Supplier;

/**
 * Assigns to supplier information about frequency of its running - rate. 
 * Class used in XML configuration file.
 * 
 * @author AGH AgE Team
 *
 */
public class ObservableProvider {

	@Inject
	private RxSchedulerProvider rxSchedulerProvider;
	
	private Supplier<? extends Object> supplier;
	private long rate;

	public ObservableProvider(Supplier<? extends Object> supplier, long rate) {
		checkNotNull(supplier);
		this.supplier = supplier;
		this.rate = rate;
	}

	/**
	 * Provides Observable object based on given in constructor supplier and rate.
	 * 
	 * @return
	 */
	public Observable<Timestamped<Object>> provideObservable(){
		return Monitoring.createObservableFromSupplier(supplier, rate, rxSchedulerProvider.getScheduler());
	}
}
