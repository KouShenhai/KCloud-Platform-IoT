/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.mybatisplus.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "CrudMapper", description = "CRUD Mapper")
public interface CrudMapper<ID, V, DO> extends BaseMapper<DO> {

    /**
     * 查看版本号.
     * @param id ID
     * @return 版本号
     */
    V selectVersion(ID id);

}
