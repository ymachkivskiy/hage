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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Set;

import org.jage.monitoring.handler.HandlerFactoryProvider;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

public class XmlConfigTest {

	@Mock
	private AbstractStatefulObserver observer1;
	
	@Mock
	private AbstractStatefulObserver observer2;
	
	@Mock
	private AbstractStatefulObserver observer3;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void initWithOneObserver(){
		// given
		
		HandlerFactoryProvider hfp1 = mock(HandlerFactoryProvider.class);
		when(hfp1.getObservers()).thenReturn(Arrays.asList(observer1));
		HandlerFactoryProvider hfp2 = mock(HandlerFactoryProvider.class);
		when(hfp2.getObservers()).thenReturn(Arrays.asList(observer1));
		
		XmlConfig xc = new XmlConfig(Arrays.asList(hfp1, hfp2));

		// when
		xc.init();
		
		// then
		Set<AbstractStatefulObserver> observers = Whitebox.getInternalState(xc, "observers");
		verify(hfp1).createAndSubscribeHandlerBasedObservableOnObservers();
		verify(hfp2).createAndSubscribeHandlerBasedObservableOnObservers();
		assertEquals(1, observers.size());
	}
	
	@Test
	public void initWithThreeObservers(){
		// given
		
		HandlerFactoryProvider hfp1 = mock(HandlerFactoryProvider.class);
		when(hfp1.getObservers()).thenReturn(Arrays.asList(observer1, observer2, observer3));
		HandlerFactoryProvider hfp2 = mock(HandlerFactoryProvider.class);
		when(hfp2.getObservers()).thenReturn(Arrays.asList(observer2, observer3));
		
		XmlConfig xc = new XmlConfig(Arrays.asList(hfp1, hfp2));
		// when
		xc.init();
		
		// then
		Set<AbstractStatefulObserver> observers = Whitebox.getInternalState(xc, "observers");
		verify(hfp1).createAndSubscribeHandlerBasedObservableOnObservers();
		verify(hfp2).createAndSubscribeHandlerBasedObservableOnObservers();
		assertEquals(3, observers.size());
	}
}
