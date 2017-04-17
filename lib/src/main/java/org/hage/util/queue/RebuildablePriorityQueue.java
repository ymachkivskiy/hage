package org.hage.util.queue;


import com.google.common.collect.ForwardingQueue;

import java.util.Collection;
import java.util.Comparator;
import java.util.Queue;

public class RebuildablePriorityQueue<E> extends ForwardingQueue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private Queue<E> queue;


    public RebuildablePriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    public RebuildablePriorityQueue(final int initialCapacity) {
        this(initialCapacity, null);
    }


    public RebuildablePriorityQueue(final int initialCapacity, final Comparator<? super E> comparator) {
        queue = new FifoPriorityQueue<E>(initialCapacity, comparator);
    }

    public RebuildablePriorityQueue(final Collection<? extends E> c) {
        queue = new FifoPriorityQueue<E>(c);
    }

    public static <T> RebuildablePriorityQueue<T> createWithComparator(final Comparator<? super T> comparator) {
        return new RebuildablePriorityQueue<T>(DEFAULT_CAPACITY, comparator);
    }

    @Override
    protected Queue<E> delegate() {
        return queue;
    }

    public void setComparator(final Comparator<E> comparator) {
        int size = queue.size();
        if (size < 1) {
            size = 11;
        }
        final Queue<E> newQueue = new FifoPriorityQueue<E>(size, comparator);
        newQueue.addAll(queue);
        queue = newQueue;
    }
}
