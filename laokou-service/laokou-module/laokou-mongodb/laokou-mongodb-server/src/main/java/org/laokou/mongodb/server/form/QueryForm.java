/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.mongodb.server.form;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.mongodb.client.dto.SearchDTO;

import java.io.Serializable;
import java.util.List;
/**
 * @author laokou
 */
@Data
@Schema(name = "QueryForm",description = "MongoDB查询实体类")
public class QueryForm implements Serializable {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 条数
     */
    private Integer pageSize = 10;

    /**
     * 模糊条件查询
     */
    private List<SearchDTO> likeSearchList;

    /**
     * 表名
     */
    private String collectionName;

    /**
     * 是否分页
     */
    private boolean needPage = false;



}
