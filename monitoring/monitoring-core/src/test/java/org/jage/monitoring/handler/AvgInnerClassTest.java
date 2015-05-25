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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rx.schedulers.Timestamped;

public class AvgInnerClassTest {

	private AvgHandlerFactory.Avg avg;
	private List<Timestamped<Object>> inputsList, expectedList ;
	
	@SuppressWarnings("unchecked")
	@Before
	public void init(){
		avg = new AvgHandlerFactory.Avg();
		inputsList = new ArrayList<Timestamped<Object>>(Arrays.asList(
				new Timestamped<Object>(0, new Double(0)),
				new Timestamped<Object>(1394286186241l, new Double(62736.2432)),
				new Timestamped<Object>(1394286371048l, new Double(95786.04581)),
				new Timestamped<Object>(1391235386497l, new Double(26.44))
		));
		expectedList = new ArrayList<Timestamped<Object>>(Arrays.asList(
				// inputsList.get(0)
				new Timestamped<Object>(0, new Double(0)),
				// inputsList.get(0) inputsList.get(1)
				new Timestamped<Object>(697143093120l, new Double(31368.1216)),
				// inputsList.get(1) // inputsList.get(2)
				new Timestamped<Object>(1394286278644l, new Double(79261.144505)),
				// inputsList.get(1) // inputsList.get(2) // inputsList.get(3)
				new Timestamped<Object>(1393269314595l, new Double(52849.57633666667))
		));
	}
	@Test
	public void testAvgZero(){
		
		//when
		Timestamped<Object> result = avg.call(inputsList.get(0));
		
		//then
		assertEquals(expectedList.get(0), result);
	}
	
	@Test
	public void testAvgZeroAndOne(){
		
		//when
		Timestamped<Object> result = avg.call(inputsList.get(0),inputsList.get(1));
		
		//then
		assertEquals(expectedList.get(1), result);
	}
	
	@Test
	public void testAvgOneAndTwo(){
		
		//when
		Timestamped<Object> result = avg.call(inputsList.get(1), inputsList.get(2));
		
		//then
		assertEquals(expectedList.get(2), result);
	}
	
	@Test
	public void testAvgOneAndTwoAndThree(){
		
		//when
		Timestamped<Object> result = avg.call(inputsList.get(1),inputsList.get(2),inputsList.get(3));
		
		//then
		assertEquals(expectedList.get(3), result);
	}
}
