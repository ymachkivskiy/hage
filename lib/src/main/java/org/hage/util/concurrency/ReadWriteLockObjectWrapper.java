package org.hage.util.concurrency;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class ReadWriteLockObjectWrapper<T> {
    private final T obj;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static <T> ReadWriteLockObjectWrapper<T> wrap(T obj) {
        return new ReadWriteLockObjectWrapper<>(obj);
    }

    public <R> R read(Function<T, R> consumer) {
        try {
            lock.readLock().lock();
            return consumer.apply(obj);
        } finally {
            lock.readLock().unlock();
        }
    }

    public <R> R writeCall(Function<T, R> consumer) {
        try {
            lock.writeLock().lock();
            return consumer.apply(obj);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void write(Consumer<T> consumer) {
        try {
            lock.writeLock().lock();
            consumer.accept(obj);
        } finally {
            lock.writeLock().unlock();
        }
    }


}
