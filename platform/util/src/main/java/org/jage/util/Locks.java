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
 * Created: 2012-04-08
 * $Id$
 */

package org.jage.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.google.common.base.Throwables;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utilities to make working with locks easier.
 * 
 * @author AGH AgE Team
 */
public final class Locks {

	private Locks() {
		// Empty
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @return an object returned by the callable.
	 * @throws Exception
	 *             an exception thrown by the callable.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 */
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

	/**
	 * Executes the provided {@link Runnable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param runnable
	 *            the runnable to execute.
	 */
	public static void withLock(final Lock lock, final Runnable runnable) {
		try {
			withLock(lock, Executors.callable(runnable));
		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @return an object returned by the callable.
	 * @throws Exception
	 *             an exception thrown by the callable.
	 * @throws InterruptedException
	 *             if lock was interrupted.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 */
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

	/**
	 * Executes the provided {@link Runnable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param runnable
	 *            the runnable to execute.
	 */
	public static void withInterruptableLock(final Lock lock, final Runnable runnable) {
		try {
			withInterruptableLock(lock, Executors.callable(runnable));
		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call. All exceptions thrown by the callable are translated into runtime exceptions.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 * @return an object returned by the callable.
	 */
	public static <T> T withLockAndRuntimeExceptions(final Lock lock, final Callable<T> callable) {
		try {
			return withLock(lock, callable);
		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call. Exceptions of the provided class are rethrown. All other exceptions thrown by
	 * the callable are translated into runtime exceptions.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @param exceptionClass
	 *            the type of the exception that can be thrown.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 * @param <E>
	 *            the type of the exception that can be thrown.
	 * 
	 * @return an object returned by the callable.
	 * 
	 * @throws E
	 *             an exception thrown by the callable.
	 */
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

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * <p>
	 * This method locks on a <em>read</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @return an object returned by the callable.
	 * @throws Exception
	 *             an exception thrown by the callable.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 */
	public static <T> T withReadLock(final ReadWriteLock lock, final Callable<T> callable) throws Exception {
		return withLock(lock.readLock(), callable);
	}
	
	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call. All exceptions thrown by the callable are translated into runtime exceptions.
	 * 
	 * <p>
	 * This method locks on a <em>read</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @return an object returned by the callable.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 */
	public static <T> T withReadLockAndRuntimeExceptions(final ReadWriteLock lock, final Callable<T> callable) {
		return withLockAndRuntimeExceptions(lock.readLock(), callable);
	}

	/**
	 * Executes the provided {@link Runnable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * <p>
	 * This method locks on a <em>read</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param runnable
	 *            the runnable to execute.
	 */
	public static void withReadLock(final ReadWriteLock lock, final Runnable runnable) {
		withLock(lock.readLock(), runnable);
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call. Exceptions of the provided class are rethrown. All other exceptions thrown by
	 * the callable are translated into runtime exceptions.
	 * 
	 * <p>
	 * This method locks on a <em>read</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @param exceptionClass
	 *            the type of the exception that can be thrown.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 * @param <E>
	 *            the type of the exception that can be thrown.
	 * 
	 * @return an object returned by the callable.
	 * 
	 * @throws E
	 *             an exception thrown by the callable.
	 */
	public static <T, E extends Exception> T withReadLockThrowing(final ReadWriteLock lock, final Callable<T> callable,
	        final Class<E> exceptionClass) throws E {
		return withLockThrowing(lock.readLock(), callable, exceptionClass);
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * <p>
	 * This method locks on a <em>write</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @return an object returned by the callable.
	 * @throws Exception
	 *             an exception thrown by the callable.
	 * @throws InterruptedException
	 *             if lock was interrupted.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 */
	public static <T> T withWriteLock(final ReadWriteLock lock, final Callable<T> callable) throws Exception {
		return withLock(lock.writeLock(), callable);
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call. All exceptions thrown by the callable are translated into runtime exceptions.
	 * 
	 * <p>
	 * This method locks on a <em>write</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @return an object returned by the callable.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 */
	public static <T> T withWriteLockAndRuntimeExceptions(final ReadWriteLock lock, final Callable<T> callable) {
		return withLockAndRuntimeExceptions(lock.writeLock(), callable);
	}
	
	/**
	 * Executes the provided {@link Runnable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call.
	 * 
	 * <p>
	 * This method locks on a <em>write</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param runnable
	 *            the runnable to execute.
	 */
	public static void withWriteLock(final ReadWriteLock lock, final Runnable runnable) {
		withLock(lock.writeLock(), runnable);
	}

	/**
	 * Executes the provided {@link Callable} with locked lock. The lock is locked before execution and guaranteed to be
	 * unlocked after finishing the call. Exceptions of the provided class are rethrown. All other exceptions thrown by
	 * the callable are translated into runtime exceptions.
	 * 
	 * <p>
	 * This method locks on a <em>write</em> lock of the {@link ReadWriteLock}.
	 * 
	 * @param lock
	 *            the lock to use.
	 * @param callable
	 *            the callable to execute.
	 * @param exceptionClass
	 *            the type of the exception that can be thrown.
	 * @param <T>
	 *            a type of the value returned by the callable.
	 * @param <E>
	 *            the type of the exception that can be thrown.
	 * 
	 * @return an object returned by the callable.
	 * 
	 * @throws E
	 *             an exception thrown by the callable.
	 */
	public static <T, E extends Exception> T withWriteLockThrowing(final ReadWriteLock lock,
	        final Callable<T> callable, final Class<E> exceptionClass) throws E {
		return withLockThrowing(lock.writeLock(), callable, exceptionClass);
	}
}
