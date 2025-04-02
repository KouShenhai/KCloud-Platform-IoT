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

package org.laokou.reactor.handler;

import org.laokou.reactor.handler.event.UnsubscribeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;

import java.util.Objects;

/**
 * @author laokou
 */
@Component
public class EventHandler {

	@Async
	@EventListener
	public void onUnsubscribeEvent(UnsubscribeEvent event) throws InterruptedException {
		Thread.sleep(event.getMilliseconds());
		Disposable disposable = event.getDisposable();
		if (!Objects.isNull(disposable) && !disposable.isDisposed()) {
			// 显式取消订阅
			disposable.dispose();
		}
	}

}
