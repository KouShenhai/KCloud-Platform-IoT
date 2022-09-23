/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.datasource.aspect;
import org.laokou.datasource.DynamicContextHolder;
import org.laokou.datasource.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
/**
 * 多数据源，切面处理类
 *
 * @author Kou Shenhai
 * @since 1.0.0
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(org.laokou.datasource.annotation.DataSource) " + "|| @within(org.laokou.datasource.annotation.DataSource)")
    public void dataSourcePointCut() {}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        DataSource targetDataSource = (DataSource)targetClass.getAnnotation(DataSource.class);
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        if (methodDataSource == null) {
            methodDataSource = AnnotationUtils.findAnnotation(method,DataSource.class);
        }
        if(targetDataSource != null || methodDataSource != null){
            String value;
            if(methodDataSource != null) {
                value = methodDataSource.value();
            } else {
                value = targetDataSource.value();
            }
            DynamicContextHolder.push(value);
            logger.debug("set datasource is {}", value);
        }
        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
            logger.debug("clean datasource");
        }
    }
}
