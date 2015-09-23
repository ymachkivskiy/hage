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
 * Created: 2012-05-06
 * $Id$
 */

package org.jage.util;


import com.google.common.collect.AbstractIterator;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A wrapper for {@link PriorityQueue} which breaks comparition ties according to the order of insertion.
 *
 * @param <E> the type of elements held in this collection.
 * @author AGH AgE Team
 * @since 2.6
 */
public class FifoPriorityQueue<E> extends AbstractQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private final PriorityQueue<FifoEntry> queue;
    private final AtomicLong seq = new AtomicLong();

    /**
     * Creates a <tt>FifoPriorityQueue</tt>, with a default initial capacity (11), which orders its elements according
     * to their {@linkplain Comparable natural ordering}.
     */
    public FifoPriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates a <tt>FifoPriorityQueue</tt>, with the specified initial capacity, which orders its elements according to
     * their {@linkplain Comparable natural ordering}.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @throws IllegalArgumentException if <tt>initialCapacity</tt> is less than 1
     */
    public FifoPriorityQueue(final int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Creates a <tt>FifoPriorityQueue</tt>, with the specified initial capacity, which orders its elements according to
     * the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator      the comparator that will be used to order this priority queue. If {@code null}, the
     *                        {@linkplain Comparable natural ordering} of the elements will be used.
     * @throws IllegalArgumentException if <tt>initialCapacity</tt> is less than 1
     */
    public FifoPriorityQueue(final int initialCapacity, final Comparator<? super E> comparator) {
        queue = new PriorityQueue<FifoEntry>(initialCapacity, comparator == null ? null : new FifoEntryComparator(
                comparator));
    }

    /**
     * Creates a <tt>FifoPriorityQueue</tt>, with a default initial capacity (11), which orders its elements according
     * to the specified comparator.
     *
     * @param comparator the comparator that will be used to order this priority queue. If {@code null}, the
     *                   {@linkplain Comparable natural ordering} of the elements will be used.
     */
    public FifoPriorityQueue(final Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    /**
     * Creates a <tt>FifoPriorityQueue</tt> containing the elements in the specified collection. If the specified
     * collection is a {@link SortedSet} or a {@link PriorityQueue}, this priority queue will be ordered according to
     * the same ordering. Otherwise, this priority queue will be ordered according to the {@linkplain Comparable natural
     * ordering} of its elements.
     *
     * @param c the collection which elements are to be put into this priority queue
     * @throws ClassCastException   if the elements of the specified collection cannot be mutually compared according to the priority
     *                              queue's ordering
     * @throws NullPointerException if the specified collection or any of its elements are null
     */
    public FifoPriorityQueue(final Collection<? extends E> c) {
        this(c.size(), getComparatorOrNull(c));
        addAll(c);
    }

    @SuppressWarnings("unchecked")
    private static <E> Comparator<? super E> getComparatorOrNull(final Collection<? extends E> c) {
        if(c instanceof SortedSet) {
            return (Comparator<? super E>) ((SortedSet<? extends E>) c).comparator();
        } else if(c instanceof PriorityQueue) {
            return (Comparator<? super E>) ((PriorityQueue<? extends E>) c).comparator();
        } else {
            return null;
        }
    }

    @Override
    public boolean offer(final E e) {
        return queue.offer(new FifoEntry(e));
    }

    @Override
    public E poll() {
        FifoEntry entry = queue.poll();
        return entry == null ? null : entry.value;
    }

    @Override
    public E peek() {
        FifoEntry entry = queue.peek();
        return entry == null ? null : entry.value;
    }

    @Override
    public Iterator<E> iterator() {
        return new AbstractIterator<E>() {

            private final Iterator<FifoEntry> iterator = queue.iterator();

            @Override
            protected E computeNext() {
                if(iterator.hasNext()) {
                    return iterator.next().value;
                } else {
                    return endOfData();
                }
            }
        };
    }

    @Override
    public int size() {
        return queue.size();
    }


    private final class FifoEntryComparator implements Comparator<FifoEntry> {

        private final Comparator<? super E> comparator;

        private FifoEntryComparator(final Comparator<? super E> comparator) {
            this.comparator = checkNotNull(comparator);
        }

        @Override
        public int compare(final FifoEntry o1, final FifoEntry o2) {
            int res = comparator.compare(o1.value, o2.value);
            if(res == 0 && o1.value != o2.value) {
                res = (o1.seqNum < o2.seqNum ? -1 : 1);
            }
            return res;
        }
    }


    private final class FifoEntry implements Comparable<FifoEntry> {

        private final long seqNum = seq.getAndIncrement();

        private final E value;

        private FifoEntry(final E value) {
            this.value = value;
        }

        @Override
        public int compareTo(final FifoEntry other) {
            @SuppressWarnings("unchecked")
            // ClassCastException is appropriate
                    int res = ((Comparable<? super E>) this.value).compareTo(other.value);
            if(res == 0 && other.value != this.value) {
                res = (this.seqNum < other.seqNum ? -1 : 1);
            }
            return res;
        }
    }
}
