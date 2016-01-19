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
 * File: DefaultValueExtractor.java
 * Created: 10-07-2013
 * Author: kamilk
 * $Id$
 */

package org.hage.monitoring.supplier.extractor;


import com.google.common.base.Optional;
import org.apache.commons.beanutils.PropertyUtils;
import org.hage.platform.component.agent.IAgent;
import org.hage.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;


/**
 * Default implementation of ValueExtractor.
 * Extracts data from a given agent using getProperty method. If this fails it use JavaBeans notation.
 *
 * @author AGH AgE Team
 */
public class DefaultValueExtractor implements ValueExtractor {

    private static final Logger log = LoggerFactory.getLogger(DefaultValueExtractor.class);

    public Optional<Object> extract(IAgent agent, String propertyName) {

        Property property = agent.getProperty(propertyName);

        Optional<Object> result = Optional.absent();

        if(property != null) {
            result = Optional.fromNullable(property.getValue());
        } else {
            try {
                result = result.or(Optional.fromNullable(PropertyUtils.getSimpleProperty(agent, propertyName)));
            } catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getMessage());

            }
        }
        return result;
    }
}