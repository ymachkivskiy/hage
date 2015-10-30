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
 * File: GatheredPropertyDao.java
 * Created: 08-09-2012
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.persistence.dao;


import org.hibernate.Transaction;
import org.jage.monitoring.persistence.config.HibernateConfiguration;
import org.jage.monitoring.persistence.model.ComputationInstance;
import org.jage.monitoring.persistence.model.Data;
import org.jage.monitoring.persistence.model.GathererName;
import org.jage.monitoring.persistence.model.Property;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;


/**
 * DAO for Property class.
 *
 * @author AGH AgE Team
 */
public class PropertyDao extends AbstractHibernateDao {

    public PropertyDao(HibernateConfiguration hibernateConfiguration) {
        super(hibernateConfiguration);
    }

    public void mergeProperty(GathererName gathererName, ComputationInstance computationInstance, long timestamp, Object data) {
        List<Data> dataList = newLinkedList();

        if(data instanceof Collection) {
            int i = 0;
            for(Object object : (Collection<Object>) data) {
                dataList.add(new Data(i, object.toString()));
                i++;
            }
        } else {
            dataList.add(new Data(0, data.toString()));
        }
        Property property = new Property(gathererName, computationInstance, timestamp, dataList);
        hibernateSession = hibernateConfiguration.openSession();
        Transaction tx = hibernateSession.beginTransaction();
        hibernateSession.merge(property);
        tx.commit();
        hibernateSession.close();
    }
}
