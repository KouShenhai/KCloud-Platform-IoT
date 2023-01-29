/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.data.filter.aspect;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.enums.SuperAdminEnum;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.data.filter.annotation.DataFilter;
import org.laokou.common.mybatisplus.entity.BasePage;
import org.laokou.auth.client.user.UserDetail;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.List;
/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class DataFilterAspect {

    @Before("@annotation(org.laokou.common.data.filter.annotation.DataFilter)")
    public void dataFilterPoint(JoinPoint point) {
        Object params = point.getArgs()[0];
        if (params instanceof BasePage basePage) {
            UserDetail userDetail = UserUtil.userDetail();
            // 超级管理员不过滤数据
            if (userDetail.getSuperAdmin() == SuperAdminEnum.YES.ordinal()) {
                return;
            }
            try {
                //否则进行数据过滤
                String sqlFilter = getSqlFilter(userDetail, point);
                basePage.setSqlFilter(sqlFilter);
            }catch (Exception ex){
                log.error("错误信息:{}",ex.getMessage());
            }
        }
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSqlFilter(UserDetail userDetail, JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataFilter dataFilter = method.getAnnotation(DataFilter.class);
        if (dataFilter == null) {
            dataFilter = AnnotationUtils.findAnnotation(method,DataFilter.class);
        }
        // 获取表的别名
        assert dataFilter != null;
        String tableAlias = dataFilter.tableAlias();
        if(StringUtil.isNotEmpty(tableAlias)){
            tableAlias +=  ".";
        }
        StringBuilder sqlFilter = new StringBuilder();
        // 用户列表
        List<Long> deptIds = userDetail.getDeptIds();
        sqlFilter.append("(");
        if (CollectionUtils.isNotEmpty(deptIds)) {
            sqlFilter.append("find_in_set(").append(tableAlias).append(dataFilter.deptId()).append(" , ").append("\"").append(StringUtil.join(deptIds,",")).append("\"").append(") or ");
        }
        sqlFilter.append(tableAlias).append(dataFilter.userId()).append(" = ").append(userDetail.getUserId());
        sqlFilter.append(")");
        return sqlFilter.toString();
    }

}
