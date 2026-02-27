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
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
	private final SpringRedisSegmentProperties properties;

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
	private volatile boolean nextReady = false;

	/**
	 * 异步加载线程池.
	 */
	private final ExecutorService loadExecutor;

	/**
	 * 初始化标识.
	 */
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	public RedisSegmentIdGenerator(RedisUtils redisUtils, SpringRedisSegmentProperties properties) {
		this.redisUtils = redisUtils;
		this.properties = properties;
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
			buffers[0] = loadSegmentFromRedis();
			buffers[1] = new Segment(0, 0);
			log.info("RedisSegmentIdGenerator initialized. key={}, step={}, first segment=[{}, {}]",
					properties.getKey(), properties.getStep(), buffers[0].cursor.get(), buffers[0].maxId);
		}
	}

	/**
	 * 关闭生成器，释放线程池.
	 */
	@Override
	public void close() {
		loadExecutor.shutdown();
		initialized.set(false);
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
				long id = current.cursor.getAndIncrement();

				// 当前号段未耗尽，直接返回
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
	public boolean isAvailable() {
		return initialized.get();
	}

	@Override
	public Instant getInstant(long snowflakeId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * 检查是否需要异步预加载下一个号段.
	 */
	private void triggerAsyncLoadIfNeeded(Segment current, long currentId) {
		long range = current.maxId - current.startId;
		long consumed = currentId - current.startId;
		double usage = (double) consumed / range;

		if (usage >= properties.getLoadFactor() && !nextReady && isLoadingNext.compareAndSet(false, true)) {
			int nextIndex = currentIndex.get() ^ 1;
			loadExecutor.submit(() -> {
				try {
					buffers[nextIndex] = loadSegmentFromRedis();
					nextReady = true;
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
			if (nextReady) {
				int newIndex = currentIndex.get() ^ 1;
				currentIndex.set(newIndex);
				nextReady = false;
				log.debug("Switched to buffer[{}]: [{}, {}]", newIndex, buffers[newIndex].cursor.get(),
						buffers[newIndex].maxId);
				return;
			}

			// 下一个号段未就绪，同步加载
			log.warn("Next segment not ready, loading synchronously...");
			int nextIndex = currentIndex.get() ^ 1;
			buffers[nextIndex] = loadSegmentFromRedis();
			currentIndex.set(nextIndex);
			nextReady = false;
			isLoadingNext.set(false);
		}
		finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 从 Redis 加载一个号段.
	 * @return 新号段
	 */
	private Segment loadSegmentFromRedis() {
		long maxId = redisUtils.execute(segmentAllocScript, Collections.singletonList(properties.getKey()),
				properties.getStep());
		long startId = maxId - properties.getStep();
		return new Segment(startId, maxId);
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
