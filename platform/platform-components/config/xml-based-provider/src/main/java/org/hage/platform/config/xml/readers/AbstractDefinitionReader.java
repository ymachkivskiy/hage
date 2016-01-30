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
 * Created: 2012-04-20
 * $Id$
 */

package org.hage.platform.config.xml.readers;


import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Abstract base class for DefinitionReaders. Allow to set nested instance and arguments readers, if needed.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractDefinitionReader<T> implements IDefinitionReader<IComponentDefinition> {

    private IDefinitionReader<IArgumentDefinition> argumentReader;

    private IDefinitionReader<IComponentDefinition> instanceReader;

    /**
     * Get the nested argument definition reader, if available.
     *
     * @return a nested argument definition reader or null
     */
    public final IDefinitionReader<IArgumentDefinition> getArgumentReader() {
        return argumentReader;
    }

    /**
     * Set the nested argument definition reader.
     *
     * @param argumentReader a nested argument definition reader
     */
    public final void setArgumentReader(final IDefinitionReader<IArgumentDefinition> argumentReader) {
        this.argumentReader = checkNotNull(argumentReader);
    }

    /**
     * Get the nested instance definition reader, if available.
     *
     * @return a nested instance definition reader or null
     */
    public final IDefinitionReader<IComponentDefinition> getInstanceReader() {
        return instanceReader;
    }

    /**
     * Set the nested instance definition reader.
     *
     * @param argumentReader a nested instance definition reader
     */
    public final void setInstanceReader(final IDefinitionReader<IComponentDefinition> instanceReader) {
        this.instanceReader = checkNotNull(instanceReader);
    }
}
