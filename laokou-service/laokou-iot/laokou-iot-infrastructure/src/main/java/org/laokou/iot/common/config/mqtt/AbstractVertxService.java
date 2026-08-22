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

package org.laokou.iot.common.config.mqtt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author laokou
 */
public abstract class AbstractVertxService<T> extends AbstractVerticle implements VertxService {

	protected final AtomicReference<Future<String>> deploymentIdFuture;

	protected AbstractVertxService(Vertx vertx) {
		super.vertx = vertx;
		deploymentIdFuture = new AtomicReference<>();
	}

	@Override
	public void deploy() {
		// 部署服务
		deploymentIdFuture.set(doDeploy());
	}

	@Override
	public void undeploy() {
		// 卸载服务
		doUndeploy();
	}

	@Override
	public void start() {
		// 启动服务
		doOpen();
	}

	@Override
	public void stop() {
		// 停止服务
		doClose();
	}

	public abstract Future<String> doDeploy();

	public abstract void doUndeploy();

	public abstract void doOpen();

	public abstract void doClose();

}
