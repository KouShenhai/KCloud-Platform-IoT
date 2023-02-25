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
package org.laokou.hbase.client.utils;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.hbase.client.annotation.HbaseFieldInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class FieldMappingUtil {

    public static List<FieldMapping> getFieldInfo(List<? extends Object> list) {
        List<FieldMapping> fieldMappingList = new ArrayList<>();
        list.stream().forEach(item -> {
            final Class<?> clazz = item.getClass();
            //返回class中的所有字段（包括私有字段）
            final Field[] fields = clazz.getDeclaredFields();
            String row = "";
            for (Field field : fields) {
                //true表示获取私有对象
                field.setAccessible(true);
                //获取字段上的FieldInfo对象
                final boolean annotationPresent = field.isAnnotationPresent(HbaseFieldInfo.class);
                if (annotationPresent) {
                    final HbaseFieldInfo annotation = field.getAnnotation(HbaseFieldInfo.class);
                    //获取字段名称
                    final String type = annotation.type();
                    final String name = annotation.name();
                    final String fieldName = field.getName();
                    if ("id".equals(type)) {
                        row = ReflectUtil.getFieldValue(item,fieldName).toString();
                    } else {
                        fieldMappingList.add(new FieldMapping(row,name,fieldName,ReflectUtil.getFieldValue(item,fieldName).toString()));
                    }
                }
            }
        });
        return fieldMappingList;
    }

}
