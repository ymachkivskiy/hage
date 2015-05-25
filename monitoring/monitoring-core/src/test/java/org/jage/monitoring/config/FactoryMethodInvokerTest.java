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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.jage.monitoring.MonitoringException;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.schedulers.Timestamped;

public class FactoryMethodInvokerTest {

	
	private FactoryMethodInvoker factoryMethodInvoker;
	
	@Before public void setUp(){
		factoryMethodInvoker = new FactoryMethodInvoker();
	}
	
	@Test
	public void shouldInvokeFactoryMethod(){
		
		// given
		ClassWithMethodToInvoke.setWasMethodInvoked(false);
		assertFalse(ClassWithMethodToInvoke.isWasMethodInvoked());
		
		// when
		factoryMethodInvoker.invokeFactoryMethod(Collections.<Observable<Timestamped<Object>>>emptyList(), "someName", "org.jage.monitoring.config.ClassWithMethodToInvoke", "methodToInvoke");
				
		// then
		assertTrue(ClassWithMethodToInvoke.isWasMethodInvoked());
	}
	
	@Test(expected=MonitoringException.class)
	public void shouldNotFindClass(){
		factoryMethodInvoker.invokeFactoryMethod(Collections.<Observable<Timestamped<Object>>>emptyList(), "someName", "org.jage.monitoring.config.ClassWithMethodToInvoke", "methodWhichDoesntExist");
	}
	
	@Test(expected=MonitoringException.class)
	public void shouldNotFindMethod(){
		// when
		factoryMethodInvoker.invokeFactoryMethod(Collections.<Observable<Timestamped<Object>>>emptyList(), "someName", "org.jage.monitoring.config.ClassWhichDoesntExist", "methodToInvoke");
	}
	
}

class ClassWithMethodToInvoke {

	private static boolean wasMethodInvoked = false;
	
	public static void methodToInvoke(List<Observable<Timestamped<Object>>> observables, String string1){
		wasMethodInvoked = true;
	}
	
	public static boolean isWasMethodInvoked() {
		return wasMethodInvoked;
	}

	public static void setWasMethodInvoked(boolean wasMethodInvoked) {
		ClassWithMethodToInvoke.wasMethodInvoked = wasMethodInvoked;
	}
}