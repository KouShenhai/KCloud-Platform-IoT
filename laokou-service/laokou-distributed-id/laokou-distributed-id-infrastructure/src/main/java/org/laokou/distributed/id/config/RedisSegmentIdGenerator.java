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

package org.laokou.distributed.id.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于 Redis 的分段 ID 生成器.
 * <p>
 * 采用双 Buffer 异步预加载方案（类似美团 Leaf-Segment）。 当前号段消耗到指定比例时，异步加载下一个号段，保证高性能和高可用。
 * </p>
 * <p>
 * 核心流程：
 * <ol>
 * <li>从 Redis 通过 Lua 脚本原子获取一个号段（INCRBY step）</li>
 * <li>本地内存中使用 AtomicLong 自增分配 ID</li>
 * <li>当前号段消耗到 loadFactor 比例时，异步预加载下一个号段到 nextBuffer</li>
 * <li>当前号段耗尽时，切换到下一个 Buffer 继续分配</li>
 * </ol>
 * </p>
 *
 * @author laokou
 */
@Slf4j
public class RedisSegmentIdGenerator implements IdGenerator {

	/**
	 * Redis 操作模板.
	 */
	private final RedisUtils redisUtils;

	/**
	 * 号段配置.
	 */
	private final SpringRedisSegmentProperties springRedisSegmentProperties;

	/**
	 * Lua 脚本.
	 */
	private final DefaultRedisScript<@NonNull Long> segmentAllocScript;

	/**
	 * 双 Buffer.
	 */
	private final Segment[] buffers = new Segment[2];

	/**
	 * 当前使用的 Buffer 索引（0 或 1）.
	 */
	private final AtomicInteger currentIndex = new AtomicInteger(0);

	/**
	 * 读写锁 — 读锁用于分配 ID，写锁用于切换 Buffer.
	 */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 是否正在异步加载下一个号段.
	 */
	private final AtomicBoolean isLoadingNext = new AtomicBoolean(false);

	/**
	 * 下一个号段是否已就绪.
	 */
	private final AtomicBoolean nextReady = new AtomicBoolean(false);

	/**
	 * 异步加载线程池.
	 */
	private final ExecutorService loadExecutor;

	/**
	 * 初始化标识.
	 */
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	/**
	 * 加载因子阈值（放大 1000 倍后的整数值），避免每次分配 ID 时进行浮点运算.
	 */
	private final int loadFactorThousandths;

	/**
	 * Redis fetch 最大重试次数.
	 */
	private static final int MAX_RETRY = 3;

	/**
	 * 重试初始退避时间（毫秒）.
	 */
	private static final long RETRY_BASE_DELAY_MS = 100;

	/**
	 * 线程池关闭等待超时时间（秒）.
	 */
	private static final int SHUTDOWN_TIMEOUT_SECONDS = 5;

	public RedisSegmentIdGenerator(RedisUtils redisUtils, SpringRedisSegmentProperties springRedisSegmentProperties) {
		this.redisUtils = redisUtils;
		this.springRedisSegmentProperties = springRedisSegmentProperties;
		this.loadFactorThousandths = (int) (springRedisSegmentProperties.getLoadFactor() * 1000);
		this.segmentAllocScript = new DefaultRedisScript<>();
		this.segmentAllocScript
			.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/segment_alloc.lua")));
		this.segmentAllocScript.setResultType(Long.class);
		this.loadExecutor = Executors.newSingleThreadExecutor(r -> {
			Thread thread = new Thread(r, "id-segment-loader");
			thread.setDaemon(true);
			return thread;
		});
	}

	/**
	 * 初始化：加载第一个号段.
	 */
	@Override
	public void init() {
		if (initialized.compareAndSet(false, true)) {
			try {
				buffers[0] = loadSegmentFromRedis();
				buffers[1] = new Segment(0, 0);
				log.info("RedisSegmentIdGenerator initialized. key={}, step={}, first segment=[{}, {}]",
						springRedisSegmentProperties.getKey(), springRedisSegmentProperties.getStep(),
						buffers[0].cursor.get(), buffers[0].maxId);
			}
			catch (Exception e) {
				// 初始化失败，回退标识，允许后续重新初始化
				initialized.set(false);
				throw e;
			}
		}
	}

	/**
	 * 关闭生成器，释放线程池.
	 */
	@Override
	public void close() {
		initialized.set(false);
		ThreadUtils.shutdown(loadExecutor, SHUTDOWN_TIMEOUT_SECONDS);
		log.info("RedisSegmentIdGenerator closed.");
	}

	@Override
	public long nextId() {
		if (!initialized.get()) {
			throw new IllegalStateException("RedisSegmentIdGenerator not initialized, please call init() first");
		}

		while (true) {
			lock.readLock().lock();
			try {
				Segment current = buffers[currentIndex.get()];
				// CAS 循环分配 ID，避免 getAndIncrement 导致 cursor 越界浪费
				long id;
				do {
					id = current.cursor.get();
					if (id >= current.maxId) {
						// 号段耗尽，跳出去走切换逻辑
						break;
					}
				}
				while (!current.cursor.compareAndSet(id, id + 1));

				if (id < current.maxId) {
					// 检查是否需要异步预加载下一个号段
					triggerAsyncLoadIfNeeded(current, id);
					return id;
				}
			}
			finally {
				lock.readLock().unlock();
			}

			// 当前号段耗尽，尝试切换到下一个 Buffer
			waitAndSwitchBuffer();
		}
	}

	@Override
	public List<Long> nextIds(int num) {
		if (!initialized.get()) {
			throw new IllegalStateException("RedisSegmentIdGenerator not initialized, please call init() first");
		}
		List<Long> ids = new ArrayList<>(num);
		while (ids.size() < num) {
			lock.readLock().lock();
			try {
				Segment current = buffers[currentIndex.get()];
				int remaining = num - ids.size();
				// CAS 批量分配：一次尝试获取尽可能多的 ID
				long start;
				long end = 0;
				do {
					start = current.cursor.get();
					if (start >= current.maxId) {
						break;
					}
					end = Math.min(start + remaining, current.maxId);
				}
				while (!current.cursor.compareAndSet(start, end));

				if (start < current.maxId) {
					for (long id = start; id < end; id++) {
						ids.add(id);
					}
					// 检查是否需要异步预加载
					triggerAsyncLoadIfNeeded(current, end - 1);
					continue;
				}
			}
			finally {
				lock.readLock().unlock();
			}

			// 当前号段耗尽，切换
			waitAndSwitchBuffer();
		}
		return ids;
	}

	@Override
	public boolean isAvailable() {
		return initialized.get();
	}

	@Override
	public Instant getInstant(long snowflakeId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * 检查是否需要异步预加载下一个号段. 使用整数乘法比较代替浮点除法，提高性能.
	 */
	private void triggerAsyncLoadIfNeeded(Segment current, long currentId) {
		long range = current.maxId - current.startId;
		long consumed = currentId - current.startId;

		// 整数比较：consumed * 1000 >= range * loadFactorThousandths
		if (consumed * 1000 >= range * loadFactorThousandths && !nextReady.get()
				&& isLoadingNext.compareAndSet(false, true)) {
			int nextIndex = currentIndex.get() ^ 1;
			loadExecutor.submit(() -> {
				try {
					buffers[nextIndex] = loadSegmentFromRedis();
					nextReady.set(true);
					log.debug("Next segment pre-loaded into buffer[{}]: [{}, {}]", nextIndex,
							buffers[nextIndex].cursor.get(), buffers[nextIndex].maxId);
				}
				catch (Exception e) {
					log.error("Failed to pre-load next segment", e);
				}
				finally {
					isLoadingNext.set(false);
				}
			});
		}
	}

	/**
	 * 当前号段耗尽，等待下一个号段就绪并切换.
	 */
	private void waitAndSwitchBuffer() {
		lock.writeLock().lock();
		try {
			// Double-check: 另一个线程可能已经切换了
			Segment current = buffers[currentIndex.get()];
			if (current.cursor.get() < current.maxId) {
				return;
			}

			// 下一个号段已就绪，直接切换
			if (nextReady.get()) {
				int newIndex = currentIndex.get() ^ 1;
				currentIndex.set(newIndex);
				nextReady.set(false);
				log.debug("Switched to buffer[{}]: [{}, {}]", newIndex, buffers[newIndex].cursor.get(),
						buffers[newIndex].maxId);
				return;
			}

			// 下一个号段未就绪，同步加载
			log.warn("Next segment not ready, loading synchronously...");
			int nextIndex = currentIndex.get() ^ 1;
			buffers[nextIndex] = loadSegmentFromRedis();
			currentIndex.set(nextIndex);
			nextReady.set(false);
			isLoadingNext.set(false);
		}
		finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 从 Redis 加载一个号段（带指数退避重试）.
	 * @return 新号段
	 */
	private Segment loadSegmentFromRedis() {
		Exception lastException = null;
		for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
			try {
				long maxId = redisUtils.execute(segmentAllocScript,
						Collections.singletonList(springRedisSegmentProperties.getKey()),
						springRedisSegmentProperties.getStep());
				long startId = maxId - springRedisSegmentProperties.getStep();
				return new Segment(startId, maxId);
			}
			catch (Exception e) {
				lastException = e;
				log.warn("Failed to load segment from Redis (attempt {}/{})", attempt, MAX_RETRY, e);
				if (attempt < MAX_RETRY) {
					try {
						// 指数退避：100ms, 200ms
						TimeUnit.MILLISECONDS.sleep(RETRY_BASE_DELAY_MS * attempt);
					}
					catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						throw new IllegalStateException("Interrupted while retrying segment load", ie);
					}
				}
			}
		}
		throw new IllegalStateException("Failed to load segment from Redis after " + MAX_RETRY + " attempts",
				lastException);
	}

	/**
	 * 号段（单个 Buffer）.
	 */
	private static class Segment {

		/**
		 * 当前分配游标（原子自增）.
		 */
		final AtomicLong cursor;

		/**
		 * 起始ID（用于计算使用比例）.
		 */
		final long startId;

		/**
		 * 号段最大值（不含）.
		 */
		final long maxId;

		Segment(long startId, long maxId) {
			this.startId = startId;
			this.cursor = new AtomicLong(startId);
			this.maxId = maxId;
		}

	}

}
