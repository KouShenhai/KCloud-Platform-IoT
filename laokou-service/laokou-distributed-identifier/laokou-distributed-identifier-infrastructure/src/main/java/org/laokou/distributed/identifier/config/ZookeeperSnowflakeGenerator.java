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

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.laokou.common.i18n.util.InstantUtils;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Slf4j
public class ZookeeperSnowflakeGenerator implements SnowflakeGenerator {

	private static final String machinePath = "/snowflake/machines";

	/**
	 * 序列标识占用的位数.
	 */
	private static final long sequenceBit = 13;

	/**
	 * 机器标识占用的位数.
	 */
	private static final long machineBit = 5;

	/**
	 * 数据标识占用的位数.
	 */
	private static final long datacenterBit = 5;

	/**
	 * 并发控制.
	 */
	private long sequence = 0L;

	/**
	 * 上一次生产ID时间戳.
	 */
	private long lastTimeStamp = -1L;

	/**
	 * 数据标识ID.
	 */
	private final long dataCenterId;

	/**
	 * 机器标识ID.
	 */
	private volatile long machineId;

	/**
	 * 起始的时间戳.
	 */
	private final long startTimestamp;

	/**
	 * Zookeeper操作工具.
	 */
	private final CuratorFramework curatorFramework;

	/**
	 * 根据指定的数据中心ID和机器标志ID生成指定的序列号.
	 * @param dataCenterId 数据中心ID
	 */
	public ZookeeperSnowflakeGenerator(final long startTimestamp, final long dataCenterId,
			CuratorFramework curatorFramework) {
		// 数据标识最大值
		long maxDatacenter = ~(-1L << datacenterBit);
		Assert.isTrue(startTimestamp <= getNextTimestamp(), "Snowflake not support current timestamp");
		Assert.isTrue(dataCenterId < maxDatacenter && dataCenterId >= 0,
				String.format("DataCenterId exceeds the maximum limit: %s", maxDatacenter));
		this.startTimestamp = startTimestamp;
		this.dataCenterId = dataCenterId;
		this.curatorFramework = curatorFramework;
	}

	/**
	 * 雪花ID生成时间.
	 * @param id 雪花ID
	 * @return 时间
	 */
	@Override
	public Instant getInstant(long id) {
		return InstantUtils.getInstantOfTimestamp(getTimestamp(id));
	}

	@Override
	public synchronized void init() throws Exception {
		// 机器标识最大值
		long maxMachine = ~(-1L << machineBit);
		// 定义分布式锁路径
		String lockPath = ZKPaths.makePath(machinePath, "lock");
		InterProcessMutex lock = new InterProcessMutex(curatorFramework, lockPath);
		try {
			// 获取分布式锁
			if (!lock.acquire(5, TimeUnit.SECONDS)) {
				throw new RuntimeException("Failed to acquire distributed lock");
			}
			// 检查并创建父路径
			if (curatorFramework.checkExists().forPath(machinePath) == null) {
				curatorFramework.create().creatingParentsIfNeeded().forPath(machinePath);
			}
			// 检查子节点数量是否超过限制
			List<String> children = curatorFramework.getChildren().forPath(machinePath);
			long machineId = children.size();
			Assert.isTrue(machineId < maxMachine, String.format("MachineId exceeds the maximum limit: %s", maxMachine));
			// 创建新的子节点
			String newNodePath = ZKPaths.makePath(machinePath, String.valueOf(machineId));
			curatorFramework.create().forPath(newNodePath, new byte[0]);
			this.machineId = machineId;
		}
		catch (KeeperException e) {
			// 捕获 KeeperException 并记录详细日志
			log.error("Zookeeper operation failed", e);
		}
		catch (Exception e) {
			// 捕获其他异常并记录日志
			log.error("Unexpected error occurred", e);
			throw new RuntimeException(e);
		}
		finally {
			// 释放分布式锁
			if (lock.isAcquiredInThisProcess()) {
				lock.release();
			}
		}
	}

	@Override
	public synchronized void close() throws Exception {
		curatorFramework.delete().forPath(ZKPaths.makePath(machinePath, String.valueOf(machineId)));
	}

	/**
	 * 生产雪花ID.
	 * @return 雪花ID
	 */
	@Override
	public synchronized long nextId() {
		int maxOffset = 5;
		long currTimeStamp = getNextTimestamp();
		if (currTimeStamp < lastTimeStamp) {
			long offset = lastTimeStamp - currTimeStamp;
			if (offset <= maxOffset) {
				try {
					wait(offset << 1);
					currTimeStamp = getNextTimestamp();
					if (currTimeStamp < lastTimeStamp) {
						throw new RuntimeException(String
							.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
					}
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else {
				throw new RuntimeException(
						String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
			}
		}
		if (currTimeStamp == lastTimeStamp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & ~(-1L << sequenceBit);
			// 同一毫秒的序列数已经达到最大
			if (sequence == 0L) {
				currTimeStamp = getNextTimestamp();
			}
		}
		else {
			// 不同毫秒内，序列号置为 1 - 3 随机数
			sequence = ThreadLocalRandom.current().nextLong(1, 3);
		}
		lastTimeStamp = currTimeStamp;
		// 左移
		return (currTimeStamp - startTimestamp) << (sequenceBit + machineBit + datacenterBit)
				// 数据标识部分
				| dataCenterId << (sequenceBit + machineBit)
				// 机器标识部分
				| machineId << sequenceBit
				// 序列标识部分
				| sequence;
	}

	private long getNextTimestamp() {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimeStamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	private long getTimestamp(long snowflakeId) {
		// 反推 -> 右移 + 开始时间戳
		return (snowflakeId >> (sequenceBit + machineBit + datacenterBit)) + startTimestamp;
	}

}
