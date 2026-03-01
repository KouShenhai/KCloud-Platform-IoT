/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.id.generator.segment;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 双 Buffer 号段容器。 使用 A/B 两个 Buffer 实现无中断切换： - 当前 Buffer 消耗到阈值时异步加载下一个 Buffer - 当前 Buffer
 * 耗尽时原子切换到已加载的备用 Buffer
 *
 * @author laokou
 */
@Slf4j
@Getter
final class SegmentBuffer {

	private final Segment[] segments = new Segment[2];

	private final AtomicInteger currentIndex = new AtomicInteger(0);

	private final AtomicBoolean loading = new AtomicBoolean(false);

	private final AtomicBoolean nextReady = new AtomicBoolean(false);

	private final ReentrantReadWriteLock switchLock = new ReentrantReadWriteLock();

	private final Lock writeLock = switchLock.writeLock();

	public Segment getCurrent() {
		return segments[currentIndex.get()];
	}

	public void setCurrent(Segment segment) {
		segments[currentIndex.get()] = segment;
	}

	public void setNext(Segment segment) {
		segments[getNextIndex()] = segment;
		nextReady.set(true);
	}

	public void switchSegment() {
		writeLock.lock();
		try {
			if (isNextReady()) {
				currentIndex.set(getNextIndex());
				nextReady.set(false);
				loading.set(false);
				log.info("Switched to next segment buffer: currentIndex={}, currentSegment=[{}, {}]",
						currentIndex.get(), getCurrent().getMinId(), getCurrent().getMaxId());
			}
		}
		finally {
			writeLock.unlock();
		}
	}

	public boolean isNotLoading() {
		return !loading.get();
	}

	public boolean isNextReady() {
		return nextReady.get();
	}

	public boolean isNotNextReady() {
		return !nextReady.get();
	}

	public boolean trySetLoading() {
		return loading.compareAndSet(false, true);
	}

	private int getNextIndex() {
		return 1 - currentIndex.get();
	}

}
