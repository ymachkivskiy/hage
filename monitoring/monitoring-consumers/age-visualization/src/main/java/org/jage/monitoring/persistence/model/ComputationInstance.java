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
package org.jage.monitoring.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Data model object represents computation instance.
 * 
 * @author AGH AgE Team
 */
@Entity
@Table(name = "compinstance")
public class ComputationInstance {

	@Column(name="compinstance_id")
	@Id
	@SequenceGenerator(name="compinstance_seq", sequenceName="compinstance_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="compinstance_seq")
	private long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="comptype_id")
	private ComputationType computationType;
	
	private String name;

	public ComputationInstance(){}
	
	public ComputationInstance(String name, ComputationType computationType) {
		this.name = name;
		this.computationType = computationType;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ComputationType getComputationType() {
		return computationType;
	}
	public void setComputationType(ComputationType computationType) {
		this.computationType = computationType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ComputationInstance)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ComputationInstance o = (ComputationInstance)obj;
		if (this.name.equals(o.getName()) && this.computationType.equals(o.getComputationType())) {
			return true;
		} else {
			return false;
		}
	}
}
