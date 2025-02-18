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

package org.laokou.logstash;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.common.kafka.template.KafkaSender;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceLogIndex;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class KafkaTest {

	private final KafkaSender reactiveKafkaSender;

	@Test
	void kafkaSenderTest() {
		TraceLogIndex index = new TraceLogIndex();
		index.setId("1");
		index.setServiceId("laokou-logstash");
		index.setProfile("dev");
		index.setDateTime(DateUtil.format(DateUtil.nowInstant(), DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS_D_SSS));
		index.setTraceId("1");
		index.setSpanId("1");
		index.setAddress("127.0.0.1");
		index.setLevel("INFO");
		index.setThreadName("main");
		index.setPackageName("org.laokou.logstash");
		index.setMessage("{\"testValue\": \"123456\"}");
		index.setStacktrace("");
		reactiveKafkaSender.send("laokou_trace_topic", JacksonUtil.toJsonStr(index)).subscribe();
	}

}
