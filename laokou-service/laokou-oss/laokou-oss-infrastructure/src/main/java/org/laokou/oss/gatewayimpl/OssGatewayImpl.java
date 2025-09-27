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

package org.laokou.oss.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.oss.template.StorageTemplate;
import org.laokou.oss.convertor.OssConvertor;
import org.laokou.oss.gateway.OssGateway;
import org.laokou.oss.gatewayimpl.database.OssMapper;
import org.laokou.oss.gatewayimpl.database.dataobject.OssDO;
import org.laokou.oss.model.OssA;
import org.laokou.oss.model.OssStatusEnum;
import org.laokou.oss.model.OssUploadV;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssGatewayImpl implements OssGateway {

	private final StorageTemplate storageTemplate;

	private final OssMapper ossMapper;

	@Override
	public OssUploadV uploadOssAndGetInfo(OssA ossA) {
		try {
			return OssConvertor.toValueObject(storageTemplate.uploadOss(
					OssConvertor.toFileInfo(ossA.getBuffer(), ossA.getSize(), ossA.getContentType(), ossA.getName(),
							ossA.getExtName()),
					OssConvertor.toBaseOssList(ossMapper.selectList(Wrappers.lambdaQuery(OssDO.class)
						.eq(OssDO::getStatus, OssStatusEnum.ENABLE.getCode())
						.select(OssDO::getParam, OssDO::getType, OssDO::getName, OssDO::getId)))));
		}
		catch (GlobalException ex) {
			throw ex;
		}
		catch (Exception e) {
			log.error("OSS上传失败，错误信息：{}", e.getMessage(), e);
			throw new BizException("B_OSS_UploadFailed", "OSS上传失败", e);
		}
	}

}
