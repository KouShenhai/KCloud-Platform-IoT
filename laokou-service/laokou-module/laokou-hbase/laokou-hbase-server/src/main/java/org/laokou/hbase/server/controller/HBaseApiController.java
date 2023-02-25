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

package org.laokou.hbase.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.hbase.client.model.CreateTableModel;
import org.laokou.hbase.client.model.HbaseModel;
import org.laokou.hbase.client.utils.HbaseFieldUtil;
import org.laokou.hbase.server.support.HbaseTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "HBase API",description = "海量存储API")
public class HBaseApiController {

    private final HbaseTemplate hbaseTemplate;

    @PostMapping("/createTable")
    @Operation(summary = "海量存储>创建表",description = "海量存储>创建表")
    public void create(@RequestBody CreateTableModel createTableModel) throws IOException {
        final String tableName = createTableModel.getTableName();
        final List<String> familyList = createTableModel.getFamilyList();
        hbaseTemplate.createTable(tableName,familyList);
    }

    @DeleteMapping("/deleteTable")
    @Operation(summary = "海量存储>删除表",description = "海量存储>删除表")
    public void delete(@RequestParam("table")String table) throws IOException {
        hbaseTemplate.dropTable(table);
    }

    @PostMapping("/get")
    @Operation(summary = "海量存储>详情",description = "海量存储>详情")
    public HttpResult<Object> get(@RequestParam("tableName")final String tableName
            ,@RequestParam("familyName")final String familyName
            , @RequestParam("rowKey")final String rowKey) throws IOException, InstantiationException, IllegalAccessException {
        return new HttpResult<>().ok(hbaseTemplate.getByRowKey(tableName,rowKey,familyName));
    }

    @PostMapping("/insertBatch")
    @Operation(summary = "海量存储>批量新增",description = "海量存储>批量新增")
    public void insertBatch(@RequestBody HbaseModel hbaseModel) throws IOException {
        final String tableName = hbaseModel.getTableName();
        final String jsonData = hbaseModel.getData();
        final List<?> objList = HbaseFieldUtil.getObjList(tableName,jsonData);
        hbaseTemplate.insertBatch(tableName, objList);
    }

}
