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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.i18n.util.DateUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import java.time.Instant;

import static org.laokou.common.mybatisplus.mapper.BaseDO.*;

/**
 * @author laokou
 */
@Slf4j
@AutoConfiguration
public class DataObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, CREATOR, UserUtils::getUserId, Long.class);
		this.strictInsertFill(metaObject, EDITOR, UserUtils::getUserId, Long.class);
		this.strictInsertFill(metaObject, CREATE_TIME, DateUtils::nowInstant, Instant.class);
		this.strictInsertFill(metaObject, UPDATE_TIME, DateUtils::nowInstant, Instant.class);
		this.strictInsertFill(metaObject, DEL_FLAG, () -> 0, Integer.class);
		this.strictInsertFill(metaObject, VERSION, () -> 0, Integer.class);
		this.strictInsertFill(metaObject, TENANT_ID, UserUtils::getTenantId, Long.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, EDITOR, UserUtils::getUserId, Long.class);
		this.strictUpdateFill(metaObject, UPDATE_TIME, DateUtils::nowInstant, Instant.class);
	}

}
