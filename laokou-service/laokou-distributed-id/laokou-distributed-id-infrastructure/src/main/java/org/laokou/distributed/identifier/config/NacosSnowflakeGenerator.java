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

package org.laokou.distributed.identifier.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.SpringContextUtils;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于 Nacos 的雪花算法生成器.
 * <p>
 * 通过 Nacos 服务注册中心自动分配 workerId 和 datacenterId，
 * 保证集群环境下每个微服务实例都有唯一的ID。
 * </p>
 * <p>
 * 位数分配方案：5位datacenter + 5位worker + 13位序列 = 1024节点，每毫秒8192个ID
 * </p>
 * @author laokou
 */
@Slf4j
public class NacosSnowflakeGenerator implements SnowflakeGenerator {

	private static final String DATACENTER_ID_KEY = "datacenterId";

	private static final String MACHINE_ID_KEY = "machineId";

	/**
	 * 序列号标识占用的位数.
	 */
	private final long sequenceBit;
	/**
	 * 机器ID占用的位数.
	 */
	private final long machineBit;
	/**
	 * 数据ID占用的位数.
	 */
	private final long datacenterBit;
	/**
	 * 机器ID最大值.
	 */
	private final long maxMachineId;
	/**
	 * 数据ID最大值.
	 */
	private final long maxDatacenterId;
	/**
	 * 序列号最大值.
	 */
	private final long maxSequence;

	/**
	 * 并发控制 - 序列号.
	 */
	private long sequence = 0L;

	/**
	 * 上一次生成ID的时间戳.
	 */
	private long lastTimestamp = -1L;

	/**
	 * 数据中心ID.
	 */
	private volatile long datacenterId;

	/**
	 * 机器ID.
	 */
	private volatile long machineId;

	/**
	 * 起始时间戳.
	 */
	private final long startTimestamp;

	/**
	 * Nacos 服务ID.
	 */
	private final String serviceId;

	/**
	 * Nacos 分组名.
	 */
	private final String groupName;

	/**
	 * Nacos 命名服务.
	 */
	private final NamingService namingService;

	/**
	 * 已分配的 machineId 缓存 (key: ip:port).
	 */
	private static final Map<String, Long> MACHINE_ID_CACHE = new ConcurrentHashMap<>();

	/**
	 * 静态 datacenter 计数器，用于分配 datacenterId.
	 */
	private static final Map<String, Long> DATACENTER_ID_CACHE = new ConcurrentHashMap<>();

	/**
	 * 初始化标识.
	 */
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	public NacosSnowflakeGenerator(NacosConfigManager nacosConfigManager, NamingService namingService, SpringSnowflakeProperties springSnowflakeProperties) {
		this.machineBit = 5L;
		this.datacenterBit = 5L;
		this.sequenceBit = 13L;
		this.maxMachineId = ~(-1L << machineBit);
		this.maxDatacenterId = ~(-1L << datacenterBit);
		this.maxSequence = ~(-1L << sequenceBit);
		this.startTimestamp = springSnowflakeProperties.getStartTimestamp();
		this.serviceId = SpringContextUtils.getServiceId();
		this.groupName = nacosConfigManager.getNacosConfigProperties().getGroup();
		this.namingService = namingService;
	}


	@Override
	public synchronized void init() throws Exception {
		if (initialized.get()) {
			log.warn("NacosSnowflakeGenerator has already been initialized.");
			return;
		}
	}

	@Override
	public synchronized void close() throws Exception {

	}

	/**
	 * 生成雪花ID.
	 *
	 * @return 雪花ID
	 */
	@Override
	public synchronized long nextId() {
		if (!initialized.get()) {
			throw new IllegalStateException("NacosSnowflakeGenerator not initialized, please call init() first");
		}

		int maxOffset = 5;
		long currentTimestamp = getCurrentTimestamp();

		// 处理时钟回拨
		if (currentTimestamp < lastTimestamp) {
			long offset = lastTimestamp - currentTimestamp;
			if (offset <= maxOffset) {
				try {
					wait(offset << 1);
					currentTimestamp = getCurrentTimestamp();
					if (currentTimestamp < lastTimestamp) {
						throw new RuntimeException(String.format(
							"Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted while waiting for clock sync", e);
				}
			} else {
				throw new RuntimeException(String.format(
					"Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
			}
		}

		// 同一毫秒内，序列号自增
		if (currentTimestamp == lastTimestamp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & maxSequence;
			// 同一毫秒的序列数已达到最大，等待下一毫秒
			if (sequence == 0L) {
				currentTimestamp = waitNextMillis(lastTimestamp);
			}
		} else {
			// 不同毫秒，序列号置为随机数（避免连续ID分布不均）
			sequence = ThreadLocalRandom.current().nextLong(1, Math.min(3, maxSequence + 1));
		}

		lastTimestamp = currentTimestamp;
		// 组装雪花ID【左移】
		return ((currentTimestamp - startTimestamp) << (sequenceBit + machineBit + datacenterBit))
			// 数据ID部分
			| (datacenterId << (sequenceBit + machineBit))
			// 机器ID部分
			| (machineId << sequenceBit)
			// 序列标识部分
			| sequence;
	}

	@Override
	public Instant getInstant(long id) {
		return InstantUtils.getInstantOfTimestamp(getTimestamp(id));
	}

	/**
	 * 从雪花ID中提取 datacenterId.
	 *
	 * @param snowflakeId 雪花ID
	 * @return datacenterId
	 */
	public long getDatacenterId(long snowflakeId) {
		return (snowflakeId >> (sequenceBit + machineBit)) & maxDatacenterId;
	}

	/**
	 * 从雪花ID中提取 workerId.
	 *
	 * @param snowflakeId 雪花ID
	 * @return workerId
	 */
	public long getWorkerId(long snowflakeId) {
		return (snowflakeId >> sequenceBit) & maxMachineId;
	}

	/**
	 * 从雪花ID中提取序列号.
	 *
	 * @param snowflakeId 雪花ID
	 * @return 序列号
	 */
	public long getSequence(long snowflakeId) {
		return snowflakeId & maxSequence;
	}

	/**
	 * 获取当前时间戳.
	 *
	 * @return 当前时间戳
	 */
	private long getCurrentTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 等待下一毫秒.
	 *
	 * @param lastTimestamp 上一次时间戳
	 * @return 下一毫秒时间戳
	 */
	private long waitNextMillis(long lastTimestamp) {
		long timestamp = getCurrentTimestamp();
		while (timestamp <= lastTimestamp) {
			timestamp = getCurrentTimestamp();
		}
		return timestamp;
	}

	/**
	 * 从雪花ID中提取时间戳.
	 *
	 * @param snowflakeId 雪花ID
	 * @return 时间戳
	 */
	private long getTimestamp(long snowflakeId) {
		// 反推 -> 右移 + 开始时间戳
		return (snowflakeId >> (sequenceBit + machineBit + datacenterBit)) + startTimestamp;
	}

}
