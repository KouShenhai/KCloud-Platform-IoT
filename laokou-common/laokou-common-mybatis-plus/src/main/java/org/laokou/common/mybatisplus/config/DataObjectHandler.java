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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import java.time.Instant;

/**
 * @author laokou
 */
@Slf4j
@AutoConfiguration
public class DataObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, BaseDO.CREATOR, UserUtils::getUserId, Long.class);
		this.strictInsertFill(metaObject, BaseDO.EDITOR, UserUtils::getUserId, Long.class);
		this.strictInsertFill(metaObject, BaseDO.CREATE_TIME, InstantUtils::now, Instant.class);
		this.strictInsertFill(metaObject, BaseDO.UPDATE_TIME, InstantUtils::now, Instant.class);
		this.strictInsertFill(metaObject, BaseDO.DEL_FLAG, () -> 0, Integer.class);
		this.strictInsertFill(metaObject, BaseDO.VERSION, () -> 0, Integer.class);
		this.strictInsertFill(metaObject, BaseDO.TENANT_ID, UserUtils::getTenantId, Long.class);
		this.strictInsertFill(metaObject, BaseDO.DEPT_ID, UserUtils::getDeptId, Long.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, BaseDO.EDITOR, UserUtils::getUserId, Long.class);
		this.strictUpdateFill(metaObject, BaseDO.UPDATE_TIME, InstantUtils::now, Instant.class);
	}

}
