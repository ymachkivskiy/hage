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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jage.monitoring.Monitoring;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Observable.class)
public class ExecutorShutdownCallerTest {

	@Mock
	private Monitoring monitoring;
	
	@Mock
	private Observable isCmp, isCmp2;
	
	@Mock
	private AbstractStatefulObserver obs, obs2;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@Ignore
	public void testInit() {
		
		when(monitoring.getAllObservers()).thenReturn(Sets.newSet(obs, obs2));
		when(obs.isCompleted()).thenReturn(isCmp);
		when(obs2.isCompleted()).thenReturn(isCmp2);
		
		ExecutorShutdownCaller esc = new ExecutorShutdownCaller();
		esc.setMonitoring(monitoring);
		
		Observable observable = mock(Observable.class);
		PowerMockito.mockStatic(Observable.class);
		when(Observable.merge(Matchers.any(Set.class))).thenReturn(observable);
		when(observable.subscribe(Matchers.any(Observer.class))).thenReturn(mock(Subscription.class));
		
		esc.init();
		verify(monitoring).getAllObservers();
		
	}
}
