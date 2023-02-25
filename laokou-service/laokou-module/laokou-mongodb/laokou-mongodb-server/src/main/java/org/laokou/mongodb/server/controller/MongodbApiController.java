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
package org.laokou.mongodb.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.mongodb.client.model.MongodbModel;
import org.laokou.mongodb.client.vo.SearchVO;
import org.laokou.mongodb.server.form.QueryForm;
import org.laokou.mongodb.client.utils.MongodbFieldUtil;
import org.laokou.mongodb.server.support.MongodbTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Mongodb API",description = "统计报表API")
public class MongodbApiController {

    private final MongodbTemplate mongodbTemplate;

    @PostMapping("/insert")
    @Operation(summary = "统计报表>新增",description = "统计报表>新增")
    public void insert(@RequestBody MongodbModel mongodbModel) {
        final String collectionName = mongodbModel.getCollectionName();
        final String jsonData = mongodbModel.getData();
        final Object obj = MongodbFieldUtil.getObj(collectionName,jsonData);
        mongodbTemplate.insert(collectionName,obj);
    }

    @PostMapping("/query")
    @Operation(summary = "统计报表>查询",description = "统计报表>查询")
    public HttpResult<SearchVO> query(@RequestBody final QueryForm queryForm) {
        return new HttpResult<SearchVO>().ok(mongodbTemplate.query(queryForm));
    }

    @PostMapping("/get")
    @Operation(summary = "统计报表>详情",description = "统计报表>详情")
    public HttpResult<Object> get(@RequestParam("collectionName")final String collectionName, @RequestParam("id")final String id) {
        final Class<?> clazz = MongodbFieldUtil.getClazz(collectionName);
        return new HttpResult<>().ok(mongodbTemplate.get(clazz,id));
    }

    @PostMapping("/insertBatch")
    @Operation(summary = "统计报表>批量新增",description = "统计报表>批量新增")
    public void insertBatch(@RequestBody MongodbModel mongodbModel) {
        final String collectionName = mongodbModel.getCollectionName();
        final String jsonData = mongodbModel.getData();
        final List<? extends Object> objList = MongodbFieldUtil.getObjList(collectionName,jsonData);
        mongodbTemplate.insertBatch(collectionName, objList);
    }

    @DeleteMapping("/all")
    @Operation(summary = "统计报表>清空",description = "统计报表>清空")
    public void deleteAll(@RequestParam("collectionName")final String collectionName ) {
        mongodbTemplate.deleteAll(collectionName);
    }

}
