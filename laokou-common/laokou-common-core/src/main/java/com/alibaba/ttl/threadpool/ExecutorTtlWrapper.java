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
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.spi.TtlEnhanced;
import com.alibaba.ttl.spi.TtlWrapper;
import io.micrometer.common.lang.NonNull;

import java.util.concurrent.Executor;

/**
 * {@link TransmittableThreadLocal} Wrapper of {@link Executor}, transmit the
 * {@link TransmittableThreadLocal} from the task submit time of {@link Runnable} to the
 * execution time of {@link Runnable}.
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @author laokou
 * @since 0.9.0
 */
public class ExecutorTtlWrapper implements Executor, TtlWrapper<Executor>, TtlEnhanced {

	private final Executor executor;

	protected final boolean idempotent;

	ExecutorTtlWrapper(Executor executor, boolean idempotent) {
		this.executor = executor;
		this.idempotent = idempotent;
	}

	@Override
	public void execute(@NonNull Runnable command) {
		executor.execute(TtlRunnable.get(command, false, idempotent));
	}

	@NonNull
	@Override
	public Executor unwrap() {
		return executor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ExecutorTtlWrapper that = (ExecutorTtlWrapper) o;

		return executor.equals(that.executor);
	}

	@Override
	public int hashCode() {
		return executor.hashCode();
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " - " + executor;
	}

}
