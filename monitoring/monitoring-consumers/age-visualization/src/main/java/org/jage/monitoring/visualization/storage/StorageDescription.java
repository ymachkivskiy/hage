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
package org.jage.monitoring.visualization.storage;

public class StorageDescription {

	private String computationType;
	private String computationInstance;
	private String gathererId;
	private String chartId;
	
	public StorageDescription(){}
	
	public StorageDescription(String computationType, String computationInstance, String gathererId, String chartId) {
	    this.computationType = computationType;
	    this.computationInstance = computationInstance;
	    this.gathererId = gathererId;
	    this.chartId = chartId;
    }
	
	public StorageDescription(String computationType, String computationInstance, String gathererId) {
	    this.computationType = computationType;
	    this.computationInstance = computationInstance;
	    this.gathererId = gathererId;
    }
	public String getComputationType() {
		return computationType;
	}
	public void setComputationType(String computationType) {
		this.computationType = computationType;
	}
	public String getComputationInstance() {
		return computationInstance;
	}
	public void setComputationInstance(String computationInstance) {
		this.computationInstance = computationInstance;
	}
	public String getGathererId() {
		return gathererId;
	}
	public void setGathererId(String gathererId) {
		this.gathererId = gathererId;
	}
	public String getChartId() {
		return chartId;
	}
	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String toString2() {
	    return computationType+"/"+computationInstance+"/"+gathererId;
	}
	
	@Override
	public String toString() {
	    return gathererId;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if(this == obj)
	    	return true;
	    StorageDescription si = (StorageDescription) obj;
	    if(computationType==null && computationInstance==null && gathererId==null)
	    	return true;
	    if(computationType.equals(si.getComputationType()) && computationInstance==null && gathererId==null)
	    	return true;
	    if(computationType.equals(si.getComputationType()) && computationInstance.equals(si.getComputationInstance()) && gathererId==null)
	    	return true;
	    if(computationType.equals(si.getComputationType()) && computationInstance.equals(si.getComputationInstance()) && gathererId.equals(si.getGathererId()))
	    	return true;
	    
		return false;
	}
	
	@Override
	public int hashCode() {
	    return 1;
	}
	
}
