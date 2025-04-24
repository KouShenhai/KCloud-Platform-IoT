/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.distributed.identifier.config.support;

import org.laokou.common.i18n.util.DateUtils;
import org.springframework.util.Assert;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author laokou
 */
public class ZookeeperSnowflakeGenerator implements SnowflakeGenerator {

	/**
	 * 序列标识占用的位数.
	 */
	private final long sequenceBit = 13;

	/**
	 * 机器标识占用的位数.
	 */
	private final long machineBit = 5;

	/**
	 * 数据标识占用的位数.
	 */
	private final long datacenterBit = 5;

	/**
	 * 机器标识ID.
	 */
	private final long machineId;

	/**
	 * 数据标识ID.
	 */
	private final long dataCenterId;

	/**
	 * 起始的时间戳.
	 */
	private final long startTimestamp;

	/**
	 * 并发控制.
	 */
	private long sequence = 0L;

	/**
	 * 上一次生产ID时间戳.
	 */
	private long lastTimeStamp = -1L;

	/**
	 * 根据指定的数据中心ID和机器标志ID生成指定的序列号.
	 * @param dataCenterId 数据中心ID
	 * @param machineId 机器标志ID
	 */
	public ZookeeperSnowflakeGenerator(final long startTimestamp, final long dataCenterId, final long machineId) {
		// 数据标识最大值
		long maxDatacenter = ~(-1L << datacenterBit);
		// 机器标识最大值
		long maxMachine = ~(-1L << machineBit);
		Assert.isTrue(startTimestamp > getNextTimestamp(), "Snowflake not support current timestamp");
		Assert.isTrue(machineId <= maxMachine && machineId >= 0,
				String.format("MachineId can't be greater than %s or less than 0", maxMachine));
		Assert.isTrue(dataCenterId <= maxDatacenter && dataCenterId >= 0,
				String.format("DtaCenterId can't be greater than %s or less than 0", maxDatacenter));
		this.startTimestamp = startTimestamp;
		this.machineId = machineId;
		this.dataCenterId = dataCenterId;
	}

	/**
	 * 雪花ID生成时间.
	 * @param id 雪花ID
	 * @return 时间
	 */
	@Override
	public Instant getInstant(long id) {
		return DateUtils.getInstantOfTimestamp(getTimestamp(id));
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
		// 第一段 时间戳部分 (反推 -> 右移 + 开始时间戳)
		return (snowflakeId >> (sequenceBit + machineBit + datacenterBit)) + startTimestamp;
	}

}
