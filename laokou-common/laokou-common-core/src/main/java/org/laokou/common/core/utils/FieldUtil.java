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
package org.laokou.common.core.utils;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.laokou.common.core.function.FieldFunction;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射相关工具类
 * @author laokou
 */
@Slf4j
public final class FieldUtil extends ReflectUtil {

    public static  <T> String getFieldName(FieldFunction<T> function) {
        try {
            String get = "get", is = "is";
            final Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            //序列化
            final SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
            String getter = serializedLambda.getImplMethodName();
            if (getter.startsWith(get)) {
                getter = getter.substring(3);
            } else if (getter.startsWith(is)) {
                getter = getter.substring(2);
            } else {
                throw new RuntimeException("缺少get|is方法");
            }
            if (StringUtil.isNotEmpty(getter)) {
                final char[] cs = getter.toCharArray();
                //首字母转小写
                cs[0] += 32;
                return String.valueOf(cs);
            }
            throw new RuntimeException("属性字段值丢失");
        } catch (Exception e) {
            log.error("获取字段名称失败 信息:{},错误:{}",e.getMessage(),e);
            return null;
        }
    }

    /**
     * 获取所有类的属性
     * @param clazz 类名
     * @param containParent 是否包含父类
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz, boolean containParent) {
        List<Field> fields = new ArrayList<>();
        if (null == clazz) {
            return null;
        }
        while (Object.class != clazz) {
            fields.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            if (containParent) {
                clazz = clazz.getSuperclass();
            } else {
                break;
            }
        }
        return fields;

    }

    /**
     * 根据属性名获取属性
     * @param field 属性名
     * @param clazz 类名
     * @param containParent 是否包含父类
     * @return
     */
    public static Field getFieldByName(String field, Class<?> clazz, boolean containParent) {
        List<Field> fields = getAllFields(clazz, containParent);

        if (CollectionUtils.isNotEmpty(fields)) {
            for (Field field1 : fields) {
                if (field1.getName().equals(field)) {
                    return field1;
                }
            }
        }
        return null;

    }

    /**
     * 通过反射获取方法名称
     * @param field
     * @param prefix
     * @return
     */
    public static String getMethod(Field field, String prefix) {
        String fieldName = field.getName();
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String getMethod(String fieldName, String prefix) {
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

}
