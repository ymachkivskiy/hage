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
/*
 * File: QueryElements.java
 * Created: 26-10-2012
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.supplier.stringquery;

import java.util.List;

/**
 * Stores the elements of agent query as list of agent names and list of property names.
 *
 * @author AGH AgE Team
 */
public class QueryElements {

	private List<String> addressList;
	private List<String> propertyNameList;
	
	public QueryElements(List<String> addressList, List<String> propertyNameList){
		this.addressList = addressList;
		this.propertyNameList = propertyNameList;
	}
	
	public List<String> getAddressList() {
		return addressList;
	}
	
	public List<String> getPropertyNameList() {
		return propertyNameList;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    return addressList.size() + propertyNameList.size();
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		QueryElements qe = (QueryElements)obj;
	    if(addressList.equals(qe.getAddressList()) && propertyNameList.equals(qe.getPropertyNameList())){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("/");
		for (String address : addressList) {
			sb.append(address).append("/");
		}
		sb.deleteCharAt(sb.length()-1);
		if(!propertyNameList.isEmpty()){
			sb.append("@");
			for (String propertyName : propertyNameList) {
				sb.append(propertyName).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}
