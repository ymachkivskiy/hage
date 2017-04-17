package org.hage.util.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class BlockingElementHolder<T> {

    private final ReentrantLock holderLock = new ReentrantLock();
    private final Condition availabilityNotifier = holderLock.newCondition();

    private T holdenElement = null;

    public boolean hasElement() {

        if (holdenElement != null) {
            return true;
        }

        try {
            holderLock.lock();

            return holdenElement != null;

        } finally {
            holderLock.unlock();
        }

    }

    /**
     *  Retrieve value from holder. Will wait till it will be available
     *
     * @return value from holder
     */
    public T acquire() {


        if (holdenElement != null) {
            return holdenElement;
        }

        try {
            holderLock.lock();

            while (holdenElement != null) {
                availabilityNotifier.await();
            }

            return holdenElement;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            holderLock.unlock();
        }
    }

    /**
     *  Value can be set only once
     *
     * @param element
     * @return true if value has been set during this call, false otherwise.
     */
    public boolean offerIfAbsent(T element) {

        if (holdenElement != null) {
            return false;
        }

        try {
            holderLock.lock();

            if (holdenElement == null) {
                holdenElement = element;
                availabilityNotifier.signalAll();

                return true;
            } else {
                return false;
            }

        } finally {
            holderLock.unlock();
        }

    }

}
