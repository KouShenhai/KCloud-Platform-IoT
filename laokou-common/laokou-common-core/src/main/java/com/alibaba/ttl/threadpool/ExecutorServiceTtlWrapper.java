/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.ttl.threadpool;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.spi.TtlEnhanced;
import io.micrometer.common.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * {@link TransmittableThreadLocal} Wrapper of {@link ExecutorService}, transmit the
 * {@link TransmittableThreadLocal} from the task submit time of {@link Runnable} or
 * {@link Callable} to the execution time of {@link Runnable} or {@link Callable}.
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @author laokou
 * @since 0.9.0
 */
public class ExecutorServiceTtlWrapper extends ExecutorTtlWrapper implements ExecutorService, TtlEnhanced {

	private final ExecutorService executorService;

	ExecutorServiceTtlWrapper(@NonNull ExecutorService executorService, boolean idempotent) {
		super(executorService, idempotent);
		this.executorService = executorService;
	}

	@Override
	public void shutdown() {
		executorService.shutdown();
	}

	@NonNull
	@Override
	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, @NonNull TimeUnit unit) throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	@NonNull
	@Override
	public <T> Future<T> submit(@NonNull Callable<T> task) {
		return executorService.submit(TtlCallable.get(task, false, idempotent));
	}

	@NonNull
	@Override
	public <T> Future<T> submit(@NonNull Runnable task, T result) {
		return executorService.submit(TtlRunnable.get(task, false, idempotent), result);
	}

	@NonNull
	@Override
	public Future<?> submit(@NonNull Runnable task) {
		return executorService.submit(TtlRunnable.get(task, false, idempotent));
	}

	@NonNull
	@Override
	public <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executorService.invokeAll(TtlCallable.gets(tasks, false, idempotent));
	}

	@NonNull
	@Override
	public <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks, long timeout,
			@NonNull TimeUnit unit) throws InterruptedException {
		return executorService.invokeAll(TtlCallable.gets(tasks, false, idempotent), timeout, unit);
	}

	@NonNull
	@Override
	public <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return executorService.invokeAny(TtlCallable.gets(tasks, false, idempotent));
	}

	@Override
	public <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks, long timeout, @NonNull TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return executorService.invokeAny(TtlCallable.gets(tasks, false, idempotent), timeout, unit);
	}

	@NonNull
	@Override
	public ExecutorService unwrap() {
		return executorService;
	}

}
