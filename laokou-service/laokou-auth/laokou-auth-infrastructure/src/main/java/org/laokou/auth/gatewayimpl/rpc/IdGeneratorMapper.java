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

package org.laokou.auth.gatewayimpl.rpc;

import io.grpc.StatusException;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.grpc.annotation.GrpcClient;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.snowflake.proto.GenerateBatchIdsRequest;
import org.laokou.snowflake.proto.GenerateBatchIdsResponse;
import org.laokou.snowflake.proto.GenerateIdRequest;
import org.laokou.snowflake.proto.GenerateIdResponse;
import org.laokou.snowflake.proto.SnowflakeServiceIGrpc;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class IdGeneratorMapper implements IdGenerator {

	@GrpcClient(serviceId = "laokou-snowflake")
	private SnowflakeServiceIGrpc.SnowflakeServiceIBlockingV2Stub snowflakeServiceIBlockingV2Stub;

	@Override
	public Long getId() {
		try {
			GenerateIdResponse generateIdResponse = snowflakeServiceIBlockingV2Stub
				.generateId(GenerateIdRequest.newBuilder().build());
			return generateIdResponse.getData();
		}
		catch (StatusException e) {
			log.error("生成雪花ID失败，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Long> getIds(int num) {
		try {
			GenerateBatchIdsResponse generateBatchIdsResponse = snowflakeServiceIBlockingV2Stub
				.generateBatchIds(GenerateBatchIdsRequest.newBuilder().setNum(num).build());
			return generateBatchIdsResponse.getDataList();
		}
		catch (StatusException e) {
			log.error("批量生成雪花IDS失败，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
