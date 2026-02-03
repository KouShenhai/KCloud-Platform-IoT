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

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 基于 Nacos 的雪花算法生成器.
 * <p>
 * 通过 Nacos 服务注册中心自动分配 machineId 和 datacenterId， 保证集群环境下每个微服务实例都有唯一的ID。
 * </p>
 * <p>
 * 位数分配方案：5位datacenter + 5位machine + 13位序列 = 1024节点，每毫秒8192个ID
 * </p>
 *
 * @author laokou
 */
@Slf4j
public class NacosSnowflakeGenerator implements SnowflakeGenerator {

	private static final String DATACENTER_ID_KEY = "datacenter_id";

	private static final String MACHINE_ID_KEY = "machine_id";

	private static final String GRPC_PORT_KEY = "grpc_port";

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
	 * 初始化标识.
	 */
	private final AtomicBoolean initialized;

	/**
	 * Spring 环境变量.
	 */
	private final Environment environment;

	/**
	 * 当前实例IP.
	 */
	private String currentIp;

	/**
	 * 当前实例端口.
	 */
	private int currentPort;

	public NacosSnowflakeGenerator(NacosConfigManager nacosConfigManager, NacosServiceManager nacosServiceManager,
			SpringSnowflakeProperties springSnowflakeProperties, Environment environment) {
		this.machineBit = 5L;
		this.datacenterBit = 5L;
		this.sequenceBit = 13L;
		this.maxMachineId = ~(-1L << machineBit);
		this.maxDatacenterId = ~(-1L << datacenterBit);
		this.maxSequence = ~(-1L << sequenceBit);
		this.startTimestamp = springSnowflakeProperties.getStartTimestamp();
		this.environment = environment;
		this.serviceId = getServiceId();
		this.groupName = nacosConfigManager.getNacosConfigProperties().getGroup();
		this.namingService = nacosServiceManager.getNamingService();
		this.initialized = new AtomicBoolean(false);
	}

	@Override
	public synchronized void init() throws Exception {
		if (initialized.get()) {
			log.warn("NacosSnowflakeGenerator has already been initialized.");
			return;
		}
		this.currentIp = InetAddress.getLocalHost().getHostAddress();
		this.currentPort = getServerPort();
		// 获取所有实例列表
		List<Instance> allInstances = namingService.getAllInstances(serviceId, groupName);
		// 分配 datacenterId 和 machineId
		allocateIds(allInstances);
		// 注册当前实例及其元数据
		registerMetadata();
		// 订阅实例变更事件
		namingService.subscribe(serviceId, groupName,
				_ -> log.debug("NacosSnowflakeGenerator received instance change event."));
		initialized.set(true);
		log.info("NacosSnowflakeGenerator initialized with datacenterId: {}, machineId: {}", datacenterId, machineId);
	}

	@Override
	public synchronized void close() throws NacosException {
		namingService.unsubscribe(serviceId, groupName,
				_ -> log.debug(
						"NacosSnowflakeGenerator unsubscribed from serviceId: {}, groupName: {}, ip: {}, port: {}",
						serviceId, groupName, currentIp, currentPort));
		initialized.set(false);
	}

	/**
	 * 生成雪花ID.
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
						throw new RuntimeException(String
							.format("Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
					}
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted while waiting for clock sync", e);
				}
			}
			else {
				throw new RuntimeException(
						String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
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
		}
		else {
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
	public Instant getInstant(long snowflakeId) {
		return InstantUtils.getInstantOfTimestamp(getTimestamp(snowflakeId));
	}

	/**
	 * 从雪花ID中提取 datacenterId.
	 * @param snowflakeId 雪花ID
	 * @return datacenterId
	 */
	public long getDatacenterId(long snowflakeId) {
		return (snowflakeId >> (sequenceBit + machineBit)) & maxDatacenterId;
	}

	/**
	 * 从雪花ID中提取 workerId.
	 * @param snowflakeId 雪花ID
	 * @return workerId
	 */
	public long getWorkerId(long snowflakeId) {
		return (snowflakeId >> sequenceBit) & maxMachineId;
	}

	/**
	 * 从雪花ID中提取序列号.
	 * @param snowflakeId 雪花ID
	 * @return 序列号
	 */
	public long getSequence(long snowflakeId) {
		return snowflakeId & maxSequence;
	}

	/**
	 * 获取当前时间戳.
	 * @return 当前时间戳
	 */
	private long getCurrentTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 等待下一毫秒.
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
	 * @param snowflakeId 雪花ID
	 * @return 时间戳
	 */
	private long getTimestamp(long snowflakeId) {
		// 反推 -> 右移 + 开始时间戳
		return (snowflakeId >> (sequenceBit + machineBit + datacenterBit)) + startTimestamp;
	}

	private int getServerPort() {
		return Integer.parseInt(environment.getProperty("server.port", System.getProperty("server.port", "9094")));
	}

	private int getGrpcServerPort() {
		return Integer.parseInt(environment.getProperty("spring.grpc.server.port",
				System.getProperty("spring.grpc.server.port", "10111")));
	}

	private String getServiceId() {
		return environment.getProperty("spring.application.name",
				System.getProperty("spring.application.name", "laokou-distributed-id"));
	}

	private void allocateIds(List<Instance> allInstances) {
		Set<Long> usedIds = allInstances.stream()
			.filter(instance -> !isCurrentInstance(instance))
			.map(Instance::getMetadata)
			.filter(MapUtils::isNotEmpty)
			.map(metadata -> {
				String machineIdStr = metadata.getOrDefault(MACHINE_ID_KEY, "0");
				String datacenterIdStr = metadata.getOrDefault(DATACENTER_ID_KEY, "0");
				long mi = Long.parseLong(machineIdStr);
				long di = Long.parseLong(datacenterIdStr);
				return mi * (maxMachineId + 1) + di;
			})
			.collect(Collectors.toSet());
		// 分配新的 datacenterId 和 machineId
		// mi=0,di=0,0
		// m1=0,di=31,31
		// ...
		// ...
		// ...
		// mi=31,di=31,1023
		long maxCombinedId = (maxDatacenterId + 1) * (maxMachineId + 1);
		long foundCombinedId = LongStream.range(0, maxCombinedId)
			.filter(id -> !usedIds.contains(id))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(
					String.format("No available ID slot. All %d slots are in use.", maxCombinedId)));
		this.datacenterId = foundCombinedId / (maxMachineId + 1);
		this.machineId = foundCombinedId % (maxMachineId + 1);
	}

	private void registerMetadata() throws NacosException {
		Map<String, String> metadata = Map.of(DATACENTER_ID_KEY, String.valueOf(this.datacenterId), MACHINE_ID_KEY,
				String.valueOf(this.machineId), GRPC_PORT_KEY, String.valueOf(getGrpcServerPort()));
		Instance instance = new Instance();
		instance.setIp(currentIp);
		instance.setPort(currentPort);
		instance.setMetadata(metadata);
		instance.setWeight(1.0);
		instance.setHealthy(true);
		instance.setEnabled(true);
		instance.setClusterName(getClusterName());
		// CP强一致性【永久实例】
		instance.setEphemeral(false);
		namingService.registerInstance(serviceId, groupName, instance);
		log.info("Registered instance with metadata: {}", metadata);
	}

	private boolean isCurrentInstance(Instance instance) {
		return ObjectUtils.equals(instance.getIp(), currentIp) && instance.getPort() == currentPort;
	}

	private String getClusterName() {
		return environment.getProperty("spring.cloud.nacos.discovery.cluster-name",
				System.getProperty("spring.cloud.nacos.discovery.cluster-name", "iot-cluster"));
	}

}
