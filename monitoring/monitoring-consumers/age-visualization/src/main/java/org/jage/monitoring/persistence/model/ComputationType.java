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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * Data model object represents computation type.
 *
 * @author AGH AgE Team
 */
@Entity
@Table(name = "comptype")
public class ComputationType {

    @Column(name = "comptype_id")
    @Id
    @SequenceGenerator(name = "comptype_seq", sequenceName = "comptype_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comptype_seq")
    private long id;

    private String name;

    public ComputationType() {
    }

    public ComputationType(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ComputationType)) {
            return false;
        }
        if(this == obj) {
            return true;
        }
        ComputationType o = (ComputationType) obj;
        if(this.name.equals(o.getName())) {
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

}
