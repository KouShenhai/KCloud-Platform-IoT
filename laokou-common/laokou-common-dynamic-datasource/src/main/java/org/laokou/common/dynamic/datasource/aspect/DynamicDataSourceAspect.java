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

package org.laokou.common.dynamic.datasource.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.dynamic.datasource.DynamicDataSource;
import org.laokou.common.dynamic.datasource.annotation.DS;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author laokou
 */
@Aspect
@Component
@Slf4j
public class DynamicDataSourceAspect {

    /**
     * 拦截类和方法的注解
     */
    @Around("@within(org.laokou.common.dynamic.datasource.annotation.DS) || @annotation(org.laokou.common.dynamic.datasource.annotation.DS)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DS ds = method.getAnnotation(DS.class);
        if (ds == null) {
            ds = AnnotationUtils.findAnnotation(method,DS.class);
            // 方法不存在注解，则说明在类上
            if (ds == null) {
                Class<?> targetClass = point.getTarget().getClass();
                ds = AnnotationUtils.findAnnotation(targetClass,DS.class);
            }
        }
        // 不为空则更新数据源
        if (ds != null) {
            String dataSourceValue = ds.value();
            DynamicDataSource.setDataSource(dataSourceValue);
        }
        Object proceed = point.proceed();
        DynamicDataSource.clearDataSource();
        return proceed;
    }

}
