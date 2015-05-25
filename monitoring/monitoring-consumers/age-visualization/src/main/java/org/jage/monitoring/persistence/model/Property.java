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
 * File: AgentDictionary.java
 * Created: 07-09-2012
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Data model object represents property.
 * 
 * @author AGH AgE Team
 */
@Entity
@Table(name = "property")
public class Property {
	
	@Column(name="property_id")
	@Id
	@SequenceGenerator(name="property_seq", sequenceName="property_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="property_seq")
	private long id;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "property_id", referencedColumnName = "property_id")
	private List<Data> data;
	private Long timestamp;
	
	@ManyToOne(cascade = javax.persistence.CascadeType.ALL)
	@JoinColumn(name="compinstance_id")
	private ComputationInstance computationInstance;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="gatherername_id")
	private GathererName gathererName;
	
	public Property(){}

	public Property(GathererName gathererName, ComputationInstance computationInstance, Long timestamp, List<Data> data) {
		this.gathererName = gathererName;
		this.computationInstance = computationInstance;
		this.timestamp = timestamp;
		this.data = data;
	}
}
