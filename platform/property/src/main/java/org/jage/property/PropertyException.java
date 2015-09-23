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
 * Created: 2010-06-23
 * $Id$
 */

package org.jage.property;


import org.jage.exception.AgeException;


/**
 * Exception for any issues connected with property mechanism.
 *
 * @author AGH AgE Team
 */
public class PropertyException extends AgeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param msg message
     */
    public PropertyException(final String msg) {
        super(msg);
    }

    /**
     * Constructor.
     *
     * @param e cause exception
     */
    public PropertyException(final Throwable e) {
        super(e);
    }

    /**
     * Constructor.
     *
     * @param msg message
     * @param e   cause
     */
    public PropertyException(final String msg, final Throwable e) {
        super(msg, e);
    }
}
