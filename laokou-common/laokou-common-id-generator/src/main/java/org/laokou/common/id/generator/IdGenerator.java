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

package org.laokou.common.id.generator;

import org.laokou.common.i18n.common.enums.BizType;

import java.time.Instant;
import java.util.List;

/**
 * 统一 ID 生成器接口.
 * <p>
 * 支持雪花算法和 Redis 分段两种策略，通过 gRPC 请求参数动态选择。
 * </p>
 *
 * @author laokou
 */
public interface IdGenerator {

	/**
	 * 初始化生成器.
	 * @throws Exception 初始化异常
	 */
	void init() throws Exception;

	/**
	 * 关闭生成器.
	 * @throws Exception 关闭异常
	 */
	void close() throws Exception;

	/**
	 * 生成下一个唯一ID.
	 * @return 唯一ID
	 */
	long nextId(BizType bizType);

	/**
	 * 批量生成唯一ID.
	 * @param num 数量
	 * @return ID列表
	 */
	List<Long> nextIds(BizType bizType, int num);

	/**
	 * 根据雪花ID获取生成时间.
	 * @param snowflakeId 雪花ID
	 * @return 生成时间
	 */
	Instant getInstant(long snowflakeId);

	/**
	 * 从雪花ID中提取 datacenterId.
	 * @param snowflakeId 雪花ID
	 * @return datacenterId
	 */
	long getDatacenterId(long snowflakeId);

	/**
	 * 从雪花ID中提取 workerId.
	 * @param snowflakeId 雪花ID
	 * @return workerId
	 */
	long getWorkerId(long snowflakeId);

	/**
	 * 从雪花ID中提取序列号.
	 * @param snowflakeId 雪花ID
	 * @return 序列号
	 */
	long getSequence(long snowflakeId);

}
