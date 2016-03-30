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
 * Created: 2008-10-07
 * $Id$
 */

package org.hage.platform.component.container.definition;


import com.google.common.base.Objects;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This argument definition simply refers to another component.
 *
 * @author AGH AgE Team
 */
public class ReferenceDefinition implements IArgumentDefinition {

    private static final long serialVersionUID = 6039110377522832121L;

    private final String targetName;

    /**
     * Creates a reference definition referring to the given target component.
     *
     * @param targetName name of target component
     */
    public ReferenceDefinition(final String targetName) {
        this.targetName = checkNotNull(targetName);
    }

    public String getTargetName() {
        return targetName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(targetName);
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof ReferenceDefinition) {
            final ReferenceDefinition other = (ReferenceDefinition) obj;
            return Objects.equal(targetName, other.targetName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("targetName", targetName)
                .toString();
    }
}
