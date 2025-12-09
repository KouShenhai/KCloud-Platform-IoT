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

package org.laokou.network.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * @author laokou
 */
public abstract class AbstractVertxService<T> extends AbstractVerticle implements VertxService {

	protected Future<T> serverFuture;

	protected Future<String> deploymentIdFuture;

	protected AbstractVertxService(Vertx vertx) {
		super.vertx = vertx;
	}

	@Override
	public void deploy() {
		// 部署服务
		deploymentIdFuture = doDeploy();
	}

	@Override
	public void undeploy() {
		// 卸载服务
		deploymentIdFuture = doUndeploy();
	}

	@Override
	public void start() {
		// 启动服务
		serverFuture = doStart();
	}

	@Override
	public void stop() {
		// 停止服务
		serverFuture = doStop();
	}

	public abstract Future<String> doDeploy();

	public abstract Future<String> doUndeploy();

	public abstract Future<T> doStart();

	public abstract Future<T> doStop();

}
