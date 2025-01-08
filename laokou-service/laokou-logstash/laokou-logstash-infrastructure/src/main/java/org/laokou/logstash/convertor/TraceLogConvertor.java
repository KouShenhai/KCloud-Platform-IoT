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

package org.laokou.logstash.convertor;

import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.logstash.dto.clientobject.LokiPushDTO;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceLogIndex;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
public final class TraceLogConvertor {

	private TraceLogConvertor() {
	}

	public static LokiPushDTO toDTO(List<TraceLogIndex> traceLogIndexList) {
		LokiPushDTO lokiPushDTO = new LokiPushDTO();
		List<LokiPushDTO.Stream> streams = traceLogIndexList.stream().map(TraceLogConvertor::toDTO).toList();
		lokiPushDTO.setStreams(streams);
		return lokiPushDTO;
	}

	private static LokiPushDTO.Stream toDTO(TraceLogIndex traceLogIndex) {
		Instant instant = DateUtil.parsInstant(traceLogIndex.getDateTime(), DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS_D_SSS);
		// 毫秒转纳秒
		String lokiTimestamp = String.valueOf(instant.toEpochMilli() * 1000000);
		LokiPushDTO.Label label = new LokiPushDTO.Label();
		label.setServiceId(traceLogIndex.getServiceId());
		label.setProfile(traceLogIndex.getProfile());
		label.setTraceId(traceLogIndex.getTraceId());
		label.setSpanId(traceLogIndex.getSpanId());
		label.setAddress(traceLogIndex.getAddress());
		label.setLevel(traceLogIndex.getLevel());
		label.setThreadName(traceLogIndex.getThreadName());
		label.setPackageName(traceLogIndex.getPackageName());
		LokiPushDTO.Stream stream = new LokiPushDTO.Stream();
		List<List<String>> values = new ArrayList<>(2);
		List<String> message = List.of(lokiTimestamp, traceLogIndex.getMessage());
		List<String> stacktrace = List.of(lokiTimestamp, traceLogIndex.getStacktrace());
		values.add(message);
		values.add(stacktrace);
		stream.setStream(label);
		stream.setValues(values);
		return stream;
	}

}
