package org.hage.util.queue;


import com.google.common.collect.AbstractIterator;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;


public class FifoPriorityQueue<E> extends AbstractQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;

    private final PriorityQueue<FifoEntry> queue;
    private final AtomicLong seq = new AtomicLong();


    public FifoPriorityQueue() {
        this(DEFAULT_CAPACITY);
    }


    public FifoPriorityQueue(final int initialCapacity) {
        this(initialCapacity, null);
    }


    public FifoPriorityQueue(final int initialCapacity, final Comparator<? super E> comparator) {
        queue = new PriorityQueue<FifoEntry>(initialCapacity, comparator == null ? null : new FifoEntryComparator(
                comparator));
    }


    public FifoPriorityQueue(final Comparator<? super E> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }


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
