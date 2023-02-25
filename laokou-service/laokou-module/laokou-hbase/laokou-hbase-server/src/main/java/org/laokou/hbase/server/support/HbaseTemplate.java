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
package org.laokou.hbase.server.support;
import cn.hutool.core.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.hbase.client.annotation.HbaseFieldInfo;
import org.laokou.hbase.client.utils.FieldMapping;
import org.laokou.hbase.client.utils.FieldMappingUtil;
import org.laokou.hbase.client.utils.HbaseFieldUtil;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HbaseTemplate{

    private final Connection hbaseConnection;

    public void insertBatch(String tableName, List<? extends Object> objList) throws IOException {
        final List<FieldMapping> fieldMappingList = FieldMappingUtil.getFieldInfo(objList);
        log.info("插入的数据：{}", JacksonUtil.toJsonStr(fieldMappingList));
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        List<Put> putList = Collections.synchronizedList(new ArrayList<>());
        fieldMappingList.stream().forEach(item -> {
            final String row = item.getRow();
            final String family = item.getFamily();
            final String qualifier = item.getQualifier();
            final String value = item.getValue();
            Put put = new Put(row.getBytes(StandardCharsets.UTF_8));
            put.addColumn(family.getBytes(StandardCharsets.UTF_8),qualifier.getBytes(StandardCharsets.UTF_8),value.getBytes(StandardCharsets.UTF_8));
            putList.add(put);
        });
        table.put(putList);
        table.close();
    }

    public Object getByRowKey(String tableName,String rowKey,String familyName) throws IOException, InstantiationException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes(StandardCharsets.UTF_8));
        Result result = table.get(get);
        log.info("查询结果：{}",JacksonUtil.toJsonStr(result));
        log.info("消耗时间：{}ms",(System.currentTimeMillis() - startTime));
        table.close();
        return getResult(tableName,result,familyName,rowKey);
    }

    /**
     * 获取返回数据
     * @return
     */
    private Object getResult(String tableName,Result result,String familyName,String rowKey) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = HbaseFieldUtil.getClazz(tableName);
        Field[] fields = clazz.getDeclaredFields();
        Object obj = clazz.newInstance();
        for (Field field : fields) {
            //true表示获取私有对象
            field.setAccessible(true);
            //获取字段上的FieldInfo对象
            final boolean annotationPresent = field.isAnnotationPresent(HbaseFieldInfo.class);
            if (annotationPresent) {
                final HbaseFieldInfo annotation = field.getAnnotation(HbaseFieldInfo.class);
                //获取字段名称
                final String type = annotation.type();
                final String fieldName = field.getName();
                if ("id".equals(type)) {
                    ReflectUtil.setFieldValue(obj,fieldName,rowKey);
                } else {
                    byte[] value = result.getValue(familyName.getBytes(StandardCharsets.UTF_8), fieldName.getBytes(StandardCharsets.UTF_8));
                    log.info("获取到的值:{}",value);
                    ReflectUtil.setFieldValue(obj,fieldName,new String(value));
                }
            }
        }
        return obj;
    }

    public void createTable(String tableName,List<String> familyList) throws IOException {
        //删除表
        TableName table = TableName.valueOf(tableName);
        Admin admin = hbaseConnection.getAdmin();
        dropTable(table,admin);
        if (!admin.tableExists(table)) {
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(table);
            List<ColumnFamilyDescriptor> columnFamilyDescriptorList = new ArrayList<>();
            familyList.stream().forEach(item -> {
                ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(item.getBytes()).build();
                columnFamilyDescriptorList.add(columnFamilyDescriptor);
            });
            tableDescriptorBuilder.setColumnFamilies(columnFamilyDescriptorList);
            TableDescriptor tableDescriptor = tableDescriptorBuilder.build();
            admin.createTable(tableDescriptor);
            admin.close();
            log.info("创建表：{}",tableName);
        } else {
            log.info("{}表已存在，表创建失败",tableName);
        }
    }

    public void dropTable(String tableName) throws IOException {
        Admin admin = hbaseConnection.getAdmin();
        TableName table = TableName.valueOf(tableName);
        dropTable(table,admin);
    }

    public void dropTable(TableName table,Admin admin) {
        try {
            if (admin.tableExists(table)) {
                admin.disableTable(table);
                admin.deleteTable(table);
                admin.close();
                log.info("删除表：{}",new String(table.getName()));
            } else {
                log.info("{}表不存在，表删除失败",new String(table.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
