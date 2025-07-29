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

package org.laokou.common.log.rpc;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.distributed.identifier.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.exception.StatusCode.OK;

/**
 * @author laokou
 */
@Slf4j
public class DistributedIdentifierMock extends DubboDistributedIdentifierServiceITriple.DistributedIdentifierServiceIImplBase {

	@Override
	public DistributedIdentifierResult generateSnowflake(DistributedIdentifierCmd cmd) {
		log.error("调用获取分布式ID失败，请检查Dubbo服务");
		return DistributedIdentifierResult.newBuilder()
			.setData(System.nanoTime())
			.build();
	}

	@Override
	public DistributedIdentifierBatchResult generateSnowflakeBatch(DistributedIdentifierBatchCmd cmd) {
		log.error("调用批量获取分布式ID失败，请检查Dubbo服务");
		int num = cmd.getNum();
		List<Long> list = new ArrayList<>(num);
		for (int i = 0; i < num; i++) {
			list.add(System.nanoTime());
		}
		return DistributedIdentifierBatchResult.newBuilder()
			.setCode(OK)
			.setMsg(MessageUtils.getMessage(OK))
			.addAllData(list)
			.build();
	}

}
