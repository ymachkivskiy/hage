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
 * Created: 2012-03-29
 * $Id$
 */

package org.hage.action;


import org.hage.exception.AgeException;


/**
 * {@code ActionException} is thrown whenever there is problem with actions.
 *
 * @author AGH AgE Team
 */
public class ActionException extends AgeException {

    private static final long serialVersionUID = 2267833332760534798L;

    /**
     * Constructs a new action exception.
     *
     * @param message the detail message.
     * @param cause   the cause of this exception.
     */
    public ActionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new action exception.
     *
     * @param message the detail message.
     */
    public ActionException(final String message) {
        super(message);
    }

    /**
     * Constructs a new action exception with <code>null</code> as its detail message.
     *
     * @param cause the cause of this exception.
     */
    public ActionException(final Throwable cause) {
        super(cause);
    }

}
