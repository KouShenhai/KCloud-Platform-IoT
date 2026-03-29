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

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.common.enums.BizType;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.common.id.generator.IdGenerator;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于 Redis 的分段 ID 生成器.
 * <p>
 * 采用双 Buffer 异步预加载方案（类似美团 Leaf-Segment）。 当前号段消耗到指定比例时，异步加载下一个号段，保证高性能和高可用。
 * </p>
 *
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
	 * 最大重试次数.
	 */
	private static final int MAX_RETRY = 3;

	/**
	 * 重试初始退避时间（毫秒）.
	 */
	private static final long RETRY_DELAY_MS = 100;

	/**
	 * 初始化标识.
	 */
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	private final ExecutorService virtualThreadExecutor;

	private final RedisUtils redisUtils;

	private final SpringSegmentProperties springSegmentProperties;

	private final DefaultRedisScript<@NonNull Long> segmentAllocScript;

	private final ReentrantReadWriteLock switchLock = new ReentrantReadWriteLock();

	private final Lock readLock = switchLock.readLock();

	private final Map<BizType, SegmentBuffer> buffers;

	public RedisSegmentIdGenerator(RedisUtils redisUtils, SpringSegmentProperties springSegmentProperties) {
		this.redisUtils = redisUtils;
		this.springSegmentProperties = springSegmentProperties;
		this.virtualThreadExecutor = ThreadUtils.newVirtualTaskExecutor();
		this.segmentAllocScript = new DefaultRedisScript<>();
		this.segmentAllocScript.setLocation(ResourceExtUtils.getResource("lua/segment_alloc.lua"));
		this.segmentAllocScript.setResultType(Long.class);
		this.buffers = new ConcurrentHashMap<>();
	}

	@Override
	public void init() {
		if (initialized.compareAndSet(false, true)) {
			try {
				springSegmentProperties.getConfigs()
					.forEach((bizType, config) -> loadSegmentSync(true, BizType.getByCode(bizType), null, config));
				log.info("RedisSegmentIdGenerator initialized successfully");
			}
			catch (Exception ex) {
				// 初始化失败，回退标识，允许后续重新初始化
				initialized.set(false);
				log.error("Failed to initialize RedisSegmentIdGenerator", ex);
				throw ex;
			}
		}
	}

	@Override
	public void close() {
		initialized.set(false);
		springSegmentProperties.getConfigs().forEach(this::flushCursor);
		ThreadUtils.shutdown(virtualThreadExecutor, 15);
		log.info("RedisSegmentIdGenerator closed.");
	}

	@Override
	public long nextId(BizType bizType) {
		if (!initialized.get()) {
			throw new IllegalStateException("RedisSegmentIdGenerator is not initialized. Call init() before using.");
		}
		SegmentBuffer segmentBuffer = buffers.get(bizType);
		readLock.lock();
		try {
			Segment segment = segmentBuffer.getCurrent();
			if (segment == null) {
				throw new IllegalStateException("Segment not initialized");
			}
			long id = segment.nextId();
			if (id > 0) {
				// 异步加载下一个 Buffer，提前准备好备用号段
				if (segment.isNext() && segmentBuffer.isNotLoading() && segmentBuffer.isNotNextReady()) {
					triggerAsyncLoad(bizType, segmentBuffer);
				}
				return id;
			}
		}
		finally {
			readLock.unlock();
		}
		// 当前号段耗尽，尝试切换
		switchAndRetry(bizType, segmentBuffer);
		return nextId(bizType);
	}

	@Override
	public List<Long> nextIds(BizType bizType, int num) {
		if (!initialized.get()) {
			throw new IllegalStateException("RedisSegmentIdGenerator is not initialized. Call init() before using.");
		}
		SegmentBuffer segmentBuffer = buffers.get(bizType);
		List<Long> ids = new ArrayList<>(num);
		while (ids.size() < num) {
			readLock.lock();
			try {
				Segment segment = segmentBuffer.getCurrent();
				if (segment == null) {
					throw new IllegalStateException("Segment not initialized");
				}
				List<Long> batchIds = segment.nextIds(num - ids.size());
				ids.addAll(batchIds);
				if (segment.isNext() && segmentBuffer.isNotLoading() && segmentBuffer.isNotNextReady()) {
					triggerAsyncLoad(bizType, segmentBuffer);
				}
			}
			finally {
				readLock.unlock();
			}
			if (ids.size() < num) {
				switchAndRetry(bizType, segmentBuffer);
			}
		}
		return ids;
	}

	@Override
	public Instant getInstant(long snowflakeId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getDatacenterId(long snowflakeId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getWorkerId(long snowflakeId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getSequence(long snowflakeId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void switchAndRetry(BizType bizType, SegmentBuffer segmentBuffer) {
		// 等待备用 Buffer 加载完成
		int retries = 0;
		while (segmentBuffer.isNotNextReady()) {
			if (segmentBuffer.isNotLoading()) {
				triggerAsyncLoad(bizType, segmentBuffer);
			}
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
			if (++retries > 100) { // ~10 秒超时
				throw new RuntimeException("Timeout waiting for next segment from Redis");
			}
		}
		// 切换分段
		segmentBuffer.switchSegment();
	}

	private void triggerAsyncLoad(BizType bizType, SegmentBuffer segmentBuffer) {
		if (segmentBuffer.trySetLoading()) {
			virtualThreadExecutor.execute(() -> {
				try {
					loadSegmentSync(false, bizType, segmentBuffer,
							springSegmentProperties.getConfigs().get(bizType.getCode()));
				}
				catch (Exception e) {
					log.error("Async segment load failed", e);
				}
			});
		}
	}

	private void loadSegmentSync(boolean isCurrent, BizType bizType, SegmentBuffer segmentBuffer,
			SpringSegmentProperties.SegmentConfig config) {
		Exception lastException = null;
		for (int i = 0; i < MAX_RETRY; i++) {
			try {
				Segment segment;
				if (isCurrent) {
					// 读取cursor历史位置
					String cursorKey = config.getCursorKey();
					Object o = redisUtils.get(cursorKey);
					if (o instanceof Long curId) {
						int step = config.getStep();
						long minId = ((curId - 1) / step) * step + 1;
						long maxId = minId + step - 1;
						segment = new Segment(minId, maxId, curId, config.getStep(), config.getLoadFactor());
					}
					else {
						long maxId = redisUtils.execute(segmentAllocScript, List.of(config.getKey()), config.getStep());
						segment = new Segment(maxId, config.getStep(), config.getLoadFactor());
					}
					segmentBuffer = new SegmentBuffer();
					segmentBuffer.setCurrent(segment);
					buffers.put(bizType, segmentBuffer);
				}
				else {
					Long maxId = redisUtils.execute(segmentAllocScript, List.of(config.getKey()), config.getStep());
					segment = new Segment(maxId, config.getStep(), config.getLoadFactor());
					segmentBuffer.setNext(segment);
				}
				log.info("Loaded segment: [{}, {}], step={}", segment.getMinId(), segment.getMaxId(), config.getStep());
				return;
			}
			catch (Exception ex) {
				lastException = ex;
				log.error("Failed to load segment from Redis, attempt {}/{}", i + 1, MAX_RETRY, ex);
				try {
					Thread.sleep(RETRY_DELAY_MS * (i + 1));
				}
				catch (InterruptedException iex) {
					Thread.currentThread().interrupt();
					throw new IllegalStateException("Interrupted while retrying segment load", iex);
				}
			}
		}
		throw new RuntimeException("Failed to load segment from Redis after " + MAX_RETRY + " attempts", lastException);
	}

	private void flushCursor(String bizType, SpringSegmentProperties.SegmentConfig config) {
		for (int i = 0; i < MAX_RETRY; i++) {
			try {
				redisUtils.set(config.getCursorKey(),
						buffers.get(BizType.getByCode(bizType)).getCurrent().getCursor().get(), RedisUtils.NOT_EXPIRE);
				break;
			}
			catch (Exception ex) {
				log.error("Failed to flush segment cursor from Redis", ex);
				try {
					Thread.sleep(RETRY_DELAY_MS * (i + 1));
				}
				catch (InterruptedException iex) {
					Thread.currentThread().interrupt();
					throw new IllegalStateException("Interrupted while retrying flush segment cursor", iex);
				}
			}
		}
		log.info("bizType:{}, Flushed segment cursor from Redis", bizType);
	}

}
