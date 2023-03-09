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
package org.laokou.common.jasypt.utils;
import lombok.SneakyThrows;
import org.laokou.common.jasypt.annotation.JasyptField;
import java.lang.reflect.Field;
/**
 * @author laokou
 */
public class JasyptUtil {

    @SneakyThrows
    public static void setFieldValue(Object obj) {
        // 获取class所有字段（包括私有字段）
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取字段上的注解
            boolean annotationPresent = field.isAnnotationPresent(JasyptField.class);
            if (annotationPresent) {
                // 私有属性
                field.setAccessible(true);
                Object o = field.get(obj);
                if (o == null) {
                    continue;
                }
                String data = o.toString();
                JasyptField jasypt = field.getAnnotation(JasyptField.class);
                switch (jasypt.type()) {
                    case DECRYPT -> data = AESUtil.decrypt(data);
                    case ENCRYPT -> data = AESUtil.encrypt(data);
                    default -> {}
                }
                // 属性赋值
                field.set(obj,data);
            }
        }
    }

}
