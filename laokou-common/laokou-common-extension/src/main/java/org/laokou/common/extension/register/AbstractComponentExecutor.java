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

package org.laokou.common.extension.register;

import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionCoordinate;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author fulan.zjf
 */
public abstract class AbstractComponentExecutor {

	/**
	 * Execute extension with Response.
	 *
	 * @param <R>         Response Type
	 * @param <T>         Parameter Type
	 * @param targetClz   类.
	 * @param bizScenario 业务码
	 * @param exeFunction 函数.
	 */
	public <R, T> R execute(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
		T component = locateComponent(targetClz, bizScenario);
		return exeFunction.apply(component);
	}

	public <R, T> R execute(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction) {
		return execute((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(),
			exeFunction);
	}

	/**
	 * Execute extension without Response.
	 *
	 * @param <T>         Parameter Type
	 * @param targetClz   类
	 * @param exeFunction 函数
	 * @param context     上下文
	 */
	public <T> void executeVoid(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
		T component = locateComponent(targetClz, context);
		exeFunction.accept(component);
	}

	public <T> void executeVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction) {
		executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
	}

	protected abstract <C> C locateComponent(Class<C> targetClz, BizScenario context);

}
