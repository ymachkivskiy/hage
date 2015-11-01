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

package org.hage.util;


import com.google.common.collect.ForwardingQueue;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * A wrapper for {@link PriorityBlockingQueue} that allows changing comparators in runtime.
 *
 * @param <E> the type of elements held in this collection.
 * @author AGH AgE Team
 * @since 2.6
 */
public class RebuildablePriorityQueue<E> extends ForwardingQueue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private Queue<E> queue;

    /**
     * Creates a <tt>RebuildablePriorityQueue</tt> with the default initial capacity (11) that orders its elements
     * according to their {@linkplain Comparable natural ordering}.
     */
    public RebuildablePriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates a <tt>RebuildablePriorityQueue</tt> with the specified initial capacity that orders its elements
     * according to their {@linkplain Comparable natural ordering}.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @throws IllegalArgumentException if <tt>initialCapacity</tt> is less than 1
     */
    public RebuildablePriorityQueue(final int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Creates a <tt>RebuildablePriorityQueue</tt> with the specified initial capacity that orders its elements
     * according to the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator      the comparator that will be used to order this priority queue. If {@code null}, the
     *                        {@linkplain Comparable natural ordering} of the elements will be used.
     * @throws IllegalArgumentException if <tt>initialCapacity</tt> is less than 1
     */
    public RebuildablePriorityQueue(final int initialCapacity, final Comparator<? super E> comparator) {
        queue = new FifoPriorityQueue<E>(initialCapacity, comparator);
    }

    /**
     * Creates a <tt>RebuildablePriorityQueue</tt> containing the elements in the specified collection. If the specified
     * collection is a {@link SortedSet} or a {@link PriorityQueue}, this priority queue will be ordered according to
     * the same ordering. Otherwise, this priority queue will be ordered according to the {@linkplain Comparable natural
     * ordering} of its elements.
     *
     * @param c the collection whose elements are to be placed into this priority queue
     * @throws ClassCastException   if elements of the specified collection cannot be compared to one another according to the priority
     *                              queue's ordering
     * @throws NullPointerException if the specified collection or any of its elements are null
     */
    public RebuildablePriorityQueue(final Collection<? extends E> c) {
        queue = new FifoPriorityQueue<E>(c);
    }

    /**
     * Creates a new {@code RebuildablePriorityQueue} with a given comparator and default initial capacity.
     *
     * @param comparator the comparator to use.
     * @return a new queue.
     */
    public static <T> RebuildablePriorityQueue<T> createWithComparator(final Comparator<? super T> comparator) {
        return new RebuildablePriorityQueue<T>(DEFAULT_CAPACITY, comparator);
    }

    @Override
    protected Queue<E> delegate() {
        return queue;
    }

    /**
     * Sets a new comparator for this queue.
     * <p>
     * <p>
     * Note: this operation requires a rebuild of the whole queue. It may be costly.
     *
     * @param comparator the new comparator to use.
     */
    public void setComparator(final Comparator<E> comparator) {
        int size = queue.size();
        if(size < 1) {
            size = 11;
        }
        final Queue<E> newQueue = new FifoPriorityQueue<E>(size, comparator);
        newQueue.addAll(queue);
        queue = newQueue;
    }
}
