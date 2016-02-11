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
 * Created: 2011-10-06
 * $Id$
 */

package org.hage.platform.component.execution.query;


/**
 * The interface for selectors of values.
 *
 * @param <T> A type of the object this selector operates on.
 * @param <V> A type of a value that this selector can select.
 * @author AGH AgE Team
 */
public interface IValueSelector<T, V> {

    /**
     * Selects a value of the type <code>V</code> from the object of the type <code>T</code>. The relation between the
     * object parameter and the returned value is completely implementation dependent.
     *
     * @param object An object to process.
     * @return Selected value.
     */
    V selectValue(T object);
}
