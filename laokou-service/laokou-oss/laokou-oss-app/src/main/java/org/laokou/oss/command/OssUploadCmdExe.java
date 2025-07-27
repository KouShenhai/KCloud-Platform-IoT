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

package org.laokou.oss.command;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.oss.ability.OssDomainService;
import org.laokou.oss.convertor.OssConvertor;
import org.laokou.oss.dto.OssUploadCmd;
import org.laokou.oss.dto.clientobject.OssUploadCO;
import org.laokou.oss.model.OssA;
import org.springframework.stereotype.Component;
import static org.laokou.oss.model.MqEnum.OSS_LOG_TOPIC;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssUploadCmdExe {

	private final OssDomainService ossDomainService;

	private final DomainEventPublisher domainEventPublisher;

	public Result<OssUploadCO> execute(OssUploadCmd cmd) {
		OssA ossA = OssConvertor.toEntity(cmd.getFileType(), cmd.getSize(), cmd.getExtName(), cmd.getBuffer(),
			cmd.getContentType(), cmd.getName());
		try {
			// 上传文件
			ossDomainService.uploadOss(ossA);
			return Result.ok(OssConvertor.toClientObject(ossA));
		} catch (GlobalException e) {
			return Result.fail(e.getCode(), e.getMsg());
		} finally {
			// 发布领域事件
			if (ossA.isPublishEvent()) {
				domainEventPublisher.publish(OSS_LOG_TOPIC, OssConvertor.toDomainEvent(ossA));
			}
		}
	}

}
