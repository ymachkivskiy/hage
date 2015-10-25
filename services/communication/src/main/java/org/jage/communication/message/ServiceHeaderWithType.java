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
 * Created: 2014-01-01
 * $Id$
 */

package org.jage.communication.message;


import lombok.ToString;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

@Immutable
@ToString
public class ServiceHeaderWithType<E extends Enum<E>> implements ServiceHeader {

    private final E type;
    private Long conversationId;

    private ServiceHeaderWithType(E type, @Nullable Long conversationId) {
        this.type = type;
        this.conversationId = conversationId;
    }

    public static <E extends Enum<E>> ServiceHeaderWithType<E> create(E type, Long conversationId) {
        return new ServiceHeaderWithType<>(checkNotNull(type), conversationId);
    }

    public static <E extends Enum<E>> ServiceHeaderWithType<E> create(E type) {
        return new ServiceHeaderWithType<>(checkNotNull(type), null);
    }

    public E getType() {
        return type;
    }

    @Override
    public Long getConversationId() {
        return conversationId;
    }
}
