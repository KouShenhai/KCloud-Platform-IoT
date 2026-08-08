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

package org.laokou.common.mybatisplus.config;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import org.apache.fory.config.Config;
import org.apache.fory.context.ReadContext;
import org.apache.fory.context.WriteContext;
import org.apache.fory.serializer.Serializer;
import org.apache.fory.serializer.Shareable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author laokou
 */
final class HavingClauseSerializer extends Serializer<Function.HavingClause> implements Shareable {

	private static final Class<?> HAVING_TYPE_CLASS;

	private static final Field HAVING_TYPE_FIELD;

	private static final Constructor<Function.HavingClause> CONSTRUCTOR;

	static {
		try {
			// net.sf.jsqlparser.expression.Function$HavingClause$HavingType
			HAVING_TYPE_CLASS = Class.forName("net.sf.jsqlparser.expression.Function$HavingClause$HavingType");
			HAVING_TYPE_FIELD = Function.HavingClause.class.getDeclaredField("havingType");
			HAVING_TYPE_FIELD.setAccessible(true);
			Constructor<Function.HavingClause> constructor = Function.HavingClause.class
				.getDeclaredConstructor(HAVING_TYPE_CLASS, Expression.class);
			constructor.setAccessible(true);
			CONSTRUCTOR = constructor;
		}
		catch (ReflectiveOperationException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	HavingClauseSerializer(Config config) {
		super(config, Function.HavingClause.class);
	}

	@Override
	public void write(WriteContext context, Function.HavingClause value) {
		try {
			// HavingType 不能直接引用，所以当 Enum<?> 处理
			Enum<?> havingType = (Enum<?>) HAVING_TYPE_FIELD.get(value);
			// 保存 MAX / MIN
			context.writeRef(havingType == null ? null : havingType.name());
			// expression 继续交给 Fory
			context.writeRef(value.getExpression());
		}
		catch (IllegalAccessException ex) {
			throw new IllegalStateException("Serialize HavingClause failed", ex);
		}
	}

	@Override
	public Function.HavingClause read(ReadContext context) {
		try {
			String havingTypeName = (String) context.readRef();
			Expression expression = (Expression) context.readRef();
			Object havingType = null;
			if (StringUtils.hasText(havingTypeName)) {
				havingType = enumValueOf(havingTypeName);
			}
			return CONSTRUCTOR.newInstance(havingType, expression);
		}
		catch (ReflectiveOperationException ex) {
			throw new IllegalStateException("Deserialize HavingClause failed", ex);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private static Object enumValueOf(String name) {
		return Enum.valueOf((Class) HAVING_TYPE_CLASS, name);
	}

}
