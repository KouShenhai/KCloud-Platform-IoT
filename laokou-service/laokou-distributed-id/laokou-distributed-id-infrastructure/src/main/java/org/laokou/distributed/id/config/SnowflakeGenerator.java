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

import java.time.Instant;

/**
 * 雪花算法生成器接口.
 *
 * @author laokou
 */
public interface SnowflakeGenerator {

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
	 * 生成下一个雪花ID.
	 * @return 雪花ID
	 */
	long nextId();

	/**
	 * 根据雪花ID获取生成时间.
	 * @param id 雪花ID
	 * @return 生成时间
	 */
	Instant getInstant(long id);

}
