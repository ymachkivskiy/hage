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
 * Created: 2011-07-27
 * $Id$
 */

package org.jage.address.agent;


import org.jage.address.node.NodeAddressSupplier;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A default agent address supplier.
 * <p>
 * This supplier is able to generate user friendly names on the base of a provided template. To ensure uniqueness of
 * user friendly names, the templates may contain wildcards, which will be replaced with consecutive numbers.
 * <p>
 * <p>
 * This implementation is thread-safe.
 *
 * @author AGH AgE Team
 */
@ThreadSafe
public class DefaultAgentAddressSupplier implements AgentAddressSupplier {

    private static final String DEFAULT_NAME_TEMPLATE = "agent*";
    private final String nameTemplate;
    private final AtomicLong nameTemplateCounter = new AtomicLong();
    @Inject
    private NodeAddressSupplier nodeAddressSupplier;

    public DefaultAgentAddressSupplier() {
        this(DEFAULT_NAME_TEMPLATE);
    }

    public DefaultAgentAddressSupplier(final String nameTemplate) {
        this.nameTemplate = checkNotNull(nameTemplate);
    }

    @Override
    public DefaultAgentAddress get() {
        return new DefaultAgentAddress(nodeAddressSupplier.get(), generateName());
    }

    private String generateName() {
        return nameTemplate.replace("*", Long.toString(nameTemplateCounter.getAndIncrement()));
    }
}
