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

package org.laokou.common.i18n.utils;

import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import static org.laokou.common.i18n.common.constant.StringConstant.AT;

/**
 * 分布式高效有序 ID 生产黑科技(sequence)
 *
 * <p>
 * 优化开源项目：<a href="https://gitee.com/yu120/sequence">...</a>
 * </p>
 *
 * @author hubin
 * @author laokou
 * @since 2016-08-18
 */
public final class IdGenerator {

	/**
	 * 雪花算法.
	 */
	private static final Snowflake INSTANCE;

	static {
		try {
			INSTANCE = new Snowflake(InetAddress.getLocalHost());
		}
		catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 默认雪花ID.
	 * @return long
	 */
	public static long defaultSnowflakeId() {
		return INSTANCE.nextId();
	}

	/**
	 * 雪花ID生成时间.
	 * @param snowflakeId 雪花ID
	 * @return 时间
	 */
	public static Instant getInstant(long snowflakeId) {
		return DateUtil.getInstantOfTimestamp(getTimestamp(snowflakeId));
	}

	/**
	 * 雪花ID生成时间.
	 * @param snowflakeId 雪花ID
	 * @return 时间
	 */
	public static LocalDateTime getLocalDateTime(long snowflakeId, ZoneId zoneId) {
		return DateUtil.getLocalDateTimeOfTimestamp(getTimestamp(snowflakeId), zoneId);
	}

	private static long getTimestamp(long snowflakeId) {
		// 第一段 时间戳部分 (反推 -> 右移left + start)
		return (snowflakeId >> Snowflake.TIMESTAMP_LEFT) + Snowflake.START_TIMESTAMP;
	}

	static class Snowflake {

		/**
		 * 起始的时间戳.
		 */
		private final static long START_TIMESTAMP = 1480166465631L;

		/**
		 * 序列标识占用的位数.
		 */
		private final static long SEQUENCE_BIT = 13;

		/**
		 * 机器标识占用的位数.
		 */
		private final static long MACHINE_BIT = 5;

		/**
		 * 数据标识占用的位数.
		 */
		private final static long DATACENTER_BIT = 5;

		/**
		 * 机器标识最大值.
		 */
		private final static long MAX_MACHINE = ~(-1L << MACHINE_BIT);

		/**
		 * 数据标识最大值.
		 */
		private final static long MAX_DATACENTER = ~(-1L << DATACENTER_BIT);

		/**
		 * 序列标识最大值.
		 */
		private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

		/**
		 * 机器标识向左移.
		 */
		private final static long MACHINE_LEFT = SEQUENCE_BIT;

		/**
		 * 数据标识向左移.
		 */
		private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;

		/**
		 * 时间戳向左移.
		 */
		private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT + DATACENTER_BIT;

		/**
		 * 机器标识ID.
		 */
		private final long MACHINE_ID;

		/**
		 * 数据标识ID.
		 */
		private final long DATACENTER_ID;

		/**
		 * IP地址.
		 */
		private InetAddress inetAddress;

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
		private Snowflake(final long dataCenterId, final long machineId) {
			Assert.isTrue(machineId <= MAX_MACHINE && machineId >= 0,
					String.format("MachineId can't be greater than %s or less than 0", MAX_MACHINE));
			Assert.isTrue(dataCenterId <= MAX_DATACENTER && dataCenterId >= 0,
					String.format("DtaCenterId can't be greater than %s or less than 0", MAX_DATACENTER));
			this.MACHINE_ID = machineId;
			this.DATACENTER_ID = dataCenterId;
		}

		private Snowflake(InetAddress inetAddress) {
			this.inetAddress = inetAddress;
			DATACENTER_ID = getDatacenterId();
			MACHINE_ID = getMaxMachineId(DATACENTER_ID);
		}

		private long getNextMill() {
			long mill = getNewTimeStamp();
			while (mill <= lastTimeStamp) {
				mill = getNewTimeStamp();
			}
			return mill;
		}

		private long getNewTimeStamp() {
			return SystemClock.now();
		}

		/**
		 * 数据标识ID.
		 */
		private long getDatacenterId() {
			long id = 0L;
			try {
				if (ObjectUtil.isNull(this.inetAddress)) {
					this.inetAddress = InetAddress.getLocalHost();
				}
				NetworkInterface network = NetworkInterface.getByInetAddress(this.inetAddress);
				if (ObjectUtil.isNull(network)) {
					id = 1L;
				}
				else {
					byte[] mac = network.getHardwareAddress();
					if (null != mac) {
						id = ((0x000000FF & (long) mac[mac.length - 2])
								| (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
						id = id % (Snowflake.MAX_DATACENTER + 1);
					}
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			return id;
		}

		/**
		 * 最大机器标识ID.
		 * @param datacenterId 机器标识ID
		 * @return 最大机器标识ID
		 */
		protected long getMaxMachineId(long datacenterId) {
			StringBuilder mpid = new StringBuilder();
			mpid.append(datacenterId);
			String name = ManagementFactory.getRuntimeMXBean().getName();
			if (StringUtil.isNotEmpty(name)) {
				/*
				 * GET jvmPid
				 */
				mpid.append(name.split(AT)[0]);
			}
			/*
			 * MAC + PID 的 hashcode 获取16个低位
			 */
			return (mpid.toString().hashCode() & 0xffff) % (Snowflake.MAX_MACHINE + 1);
		}

		/**
		 * 生产雪花ID.
		 * @return 雪花ID
		 */
		@SneakyThrows
		public synchronized long nextId() {
			long currTimeStamp = getNewTimeStamp();
			int maxOffset = 5;
			if (currTimeStamp < lastTimeStamp) {
				long offset = lastTimeStamp - currTimeStamp;
				if (offset <= maxOffset) {
					try {
						wait(offset << 1);
						currTimeStamp = getNewTimeStamp();
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
					throw new RuntimeException(String
						.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
				}
			}
			if (currTimeStamp == lastTimeStamp) {
				// 相同毫秒内，序列号自增
				sequence = (sequence + 1) & MAX_SEQUENCE;
				// 同一毫秒的序列数已经达到最大
				if (sequence == 0L) {
					currTimeStamp = getNextMill();
				}
			}
			else {
				// 不同毫秒内，序列号置为 1 - 2 随机数
				sequence = ThreadLocalRandom.current().nextLong(1, 3);
			}
			lastTimeStamp = currTimeStamp;
			// 时间戳部分
			return (currTimeStamp - START_TIMESTAMP) << TIMESTAMP_LEFT
					// 数据标识部分
					| DATACENTER_ID << DATACENTER_LEFT
					// 机器标识部分
					| MACHINE_ID << MACHINE_LEFT
					// 序列标识部分
					| sequence;
		}

	}

	/**
	 * 高并发场景下System.currentTimeMillis()的性能问题的优化.
	 *
	 * <p>
	 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右）
	 * </p>
	 * <p>
	 * System.currentTimeMillis()之所以慢是因为去跟系统打了一次交道
	 * </p>
	 * <p>
	 * 后台定时更新时钟，JVM退出时，线程自动回收
	 * </p>
	 * <p>
	 * 10亿：43410,206,210.72815533980582%
	 * </p>
	 * <p>
	 * 1亿：4699,29,162.0344827586207%
	 * </p>
	 * <p>
	 * 1000万：480,12,40.0%
	 * </p>
	 * <p>
	 * 100万：50,10,5.0%
	 * </p>
	 *
	 * @author hubin
	 * @since 2016-08-01
	 */
	public final static class SystemClock {

		private final long initialDelay;

		private final long period;

		private final AtomicLong now;

		private SystemClock(long initialDelay, long period) {
			this.initialDelay = initialDelay;
			this.period = period;
			this.now = new AtomicLong(System.currentTimeMillis());
			scheduleClockUpdating();
		}

		private static SystemClock instance() {
			return InstanceHolder.INSTANCE;
		}

		public static long now() {
			return instance().currentTimeMillis();
		}

		private void scheduleClockUpdating() {
			// System.currentTimeMillis() => 线程安全
			ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
				Thread thread = new Thread(runnable, "system-clock-thread");
				thread.setDaemon(false);
				return thread;
			});
			scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), initialDelay, period,
					TimeUnit.MILLISECONDS);
		}

		private long currentTimeMillis() {
			return now.get();
		}

		private static class InstanceHolder {

			public static final SystemClock INSTANCE = new SystemClock(1, 1);

		}

	}

}
