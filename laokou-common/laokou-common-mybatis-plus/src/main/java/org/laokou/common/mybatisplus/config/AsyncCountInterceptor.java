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

package org.laokou.common.mybatisplus.config;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author gitkakafu
 * @author laokou
 */
@Intercepts(value = {
		@Signature(type = Executor.class, method = "query",
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
						BoundSql.class }),
		@Signature(type = Executor.class, method = "query",
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class AsyncCountInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object obj = invocation.proceed();
		try {
			AsyncPaginationInnerInterceptor.get().join();
		}
		finally {
			AsyncPaginationInnerInterceptor.remove();
		}
		return obj;
	}

}
