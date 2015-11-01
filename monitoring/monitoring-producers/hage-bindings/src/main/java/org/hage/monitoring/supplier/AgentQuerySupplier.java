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
 * File: AgentQuery.java
 * Created: 26-08-2013
 * Author: Daniel
 * $Id$
 */

package org.hage.monitoring.supplier;


import com.google.common.base.Supplier;
import org.hage.query.IQuery;
import org.hage.workplace.Workplace;
import org.hage.workplace.manager.WorkplaceManager;

import javax.inject.Inject;
import java.util.List;


/**
 * Supplier provides data fetched from agents.
 *
 * @author AGH AgE Team
 */
public class AgentQuerySupplier implements Supplier<Object> {

    private final IQuery<List<Workplace>, ?> query;
    private WorkplaceManager workplaceManager;

    public AgentQuerySupplier(final IQuery<List<Workplace>, ?> query) {
        this.query = query;
    }

    @Inject
    public void setWorkplaceManager(final WorkplaceManager workplaceManager) {
        this.workplaceManager = workplaceManager;
    }

    @Override
    public Object get() {
        return query.execute(workplaceManager.getLocalWorkplaces());
    }
}