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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.extension.parser.cache.AbstractCaffeineJsqlParseCache;
import com.github.benmanes.caffeine.cache.Cache;

/**
 * jsqlparser 缓存 fury 序列化 Caffeine 缓存实现.
 *
 * @author laokou
 */
public class FurySerialCaffeineJsqlParseCache extends AbstractCaffeineJsqlParseCache {

	public FurySerialCaffeineJsqlParseCache(Cache<String, byte[]> cache) {
		super(cache);
	}

	@Override
	public byte[] serialize(Object obj) {
		return JSqlParserFuryFactory.getFuryFactory().serialize(obj);
	}

	@Override
	public Object deserialize(String sql, byte[] bytes) {
		return JSqlParserFuryFactory.getFuryFactory().deserialize(bytes);
	}

}
