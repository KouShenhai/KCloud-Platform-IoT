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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.i18n.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.laokou.common.mybatisplus.mapper.BaseDO.*;

/**
 * @author laokou
 */
@Component
@Slf4j
public class DataObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		UserContextHolder.User user = UserContextHolder.get();
		this.strictInsertFill(metaObject, CREATOR, user::getId, Long.class);
		this.strictInsertFill(metaObject, EDITOR, user::getId, Long.class);
		this.strictInsertFill(metaObject, CREATE_DATE, DateUtil::nowInstant, Instant.class);
		this.strictInsertFill(metaObject, UPDATE_DATE, DateUtil::nowInstant, Instant.class);
		this.strictInsertFill(metaObject, DEL_FLAG, () -> 0, Integer.class);
		this.strictInsertFill(metaObject, VERSION, () -> 0, Integer.class);
		this.strictInsertFill(metaObject, DEPT_ID, user::getDeptId, Long.class);
		this.strictInsertFill(metaObject, DEPT_PATH, user::getDeptPath, String.class);
		this.strictInsertFill(metaObject, TENANT_ID, user::getTenantId, Long.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		UserContextHolder.User user = UserContextHolder.get();
		this.strictUpdateFill(metaObject, EDITOR, user::getId, Long.class);
		this.strictUpdateFill(metaObject, UPDATE_DATE, DateUtil::nowInstant, Instant.class);
	}

}
