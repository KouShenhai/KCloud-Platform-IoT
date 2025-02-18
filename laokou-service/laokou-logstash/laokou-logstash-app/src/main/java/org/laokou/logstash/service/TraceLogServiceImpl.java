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

package org.laokou.logstash.service;

import lombok.RequiredArgsConstructor;
import org.laokou.logstash.api.TraceLogServiceI;
import org.laokou.logstash.command.TraceLogSaveCmdExe;
import org.laokou.logstash.dto.TraceLogSaveCmd;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TraceLogServiceImpl implements TraceLogServiceI {

	private final TraceLogSaveCmdExe traceLogSaveCmdExe;

	@Override
	public Mono<Void> save(TraceLogSaveCmd cmd) {
		return traceLogSaveCmdExe.executeVoid(cmd);
	}

}
