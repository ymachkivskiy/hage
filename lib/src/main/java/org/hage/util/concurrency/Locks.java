package org.hage.util.concurrency;


import com.google.common.base.Throwables;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;


public final class Locks {

    private Locks() {
        // Empty
    }

    public static void withInterruptableLock(final Lock lock, final Runnable runnable) {
        try {
            withInterruptableLock(lock, Executors.callable(runnable));
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static <T> T withInterruptableLock(final Lock lock, final Callable<T> callable) throws Exception {
        checkNotNull(lock);
        checkNotNull(callable);

        lock.lockInterruptibly();
        try {
            return callable.call();
        } finally {
            lock.unlock();
        }
    }

    public static <T> T withReadLock(final ReadWriteLock lock, final Callable<T> callable) throws Exception {
        return withLock(lock.readLock(), callable);
    }

    public static <T> T withLock(final Lock lock, final Callable<T> callable) throws Exception {
        checkNotNull(lock);
        checkNotNull(callable);

        lock.lock();
        try {
            return callable.call();
        } finally {
            lock.unlock();
        }
    }

    public static <T> T withReadLockAndRuntimeExceptions(final ReadWriteLock lock, final Callable<T> callable) {
        return withLockAndRuntimeExceptions(lock.readLock(), callable);
    }

    public static <T> T withLockAndRuntimeExceptions(final Lock lock, final Callable<T> callable) {
        try {
            return withLock(lock, callable);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }


    public static void withReadLock(final ReadWriteLock lock, final Runnable runnable) {
        withLock(lock.readLock(), runnable);
    }


    public static void withLock(final Lock lock, final Runnable runnable) {
        try {
            withLock(lock, Executors.callable(runnable));
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static void withExceptionLock(final Lock lock, final RunnableWithException runnable) {
        try {
            withLock(lock, () -> {
                runnable.run();
                return null;
            });
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }


    public static <T, E extends Exception> T withReadLockThrowing(final ReadWriteLock lock, final Callable<T> callable,
                                                                  final Class<E> exceptionClass) throws E {
        return withLockThrowing(lock.readLock(), callable, exceptionClass);
    }


    public static <T, E extends Exception> T withLockThrowing(final Lock lock, final Callable<T> callable,
                                                              final Class<E> exceptionClass) throws E {
        checkNotNull(exceptionClass);

        try {
            return withLock(lock, callable);
        } catch (final Exception e) {
            Throwables.propagateIfPossible(e, exceptionClass);
            throw Throwables.propagate(e);
        }
    }

    public static <T> T withWriteLock(final ReadWriteLock lock, final Callable<T> callable) throws Exception {
        return withLock(lock.writeLock(), callable);
    }

    public static <T> T withWriteLockAndRuntimeExceptions(final ReadWriteLock lock, final Callable<T> callable) {
        return withLockAndRuntimeExceptions(lock.writeLock(), callable);
    }


    public static void withWriteLock(final ReadWriteLock lock, final Runnable runnable) {
        withLock(lock.writeLock(), runnable);
    }

    public static <T, E extends Exception> T withWriteLockThrowing(final ReadWriteLock lock,
                                                                   final Callable<T> callable, final Class<E> exceptionClass) throws E {
        return withLockThrowing(lock.writeLock(), callable, exceptionClass);
    }
}
