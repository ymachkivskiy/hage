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
package org.jage.monitoring.supplier.stringquery;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryElementsTest {
	
	@Test
	public void testToStringWithTwoAgentsAndTwoProperties(){
		
		// given
		List<String> addressList = Arrays.asList("agent1", "agent2"); 
		List<String> propertyNameList = Arrays.asList("property1", "property2");
		String expected = "/agent1/agent2@property1,property2";
		
		// when
		QueryElements queryElements = new QueryElements(addressList, propertyNameList);
		
		String actual = queryElements.toString();
		
		// then
		assertEquals(expected, actual);
	}

	@Test
	public void testToStringWithNoProperty(){

		// given
		List<String> addressList = Arrays.asList("agent1", "agent2"); 
		List<String> propertyNameList = new ArrayList<>();
		String expected = "/agent1/agent2";
		
		//when
		QueryElements queryElements = new QueryElements(addressList, propertyNameList);
		
		String actual = queryElements.toString();
		
		// then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToStringWithOneAgentAndOneProperty(){
		
		// given
		List<String> addressList = Arrays.asList("agent1"); 
		List<String> propertyNameList = Arrays.asList("property1");
		String expected = "/agent1@property1";
		
		// when
		QueryElements queryElements = new QueryElements(addressList, propertyNameList);
		
		String actual = queryElements.toString();
		
		// then
		assertEquals(expected, actual);
	}
}
