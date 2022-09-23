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
import org.laokou.common.entity.BasePage;
import org.laokou.common.enums.SuperAdminEnum;
import org.laokou.common.exception.CustomException;
import org.laokou.common.exception.ErrorCode;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.HttpContextUtil;
import org.laokou.datasource.annotation.DataFilter;
import org.laokou.security.utils.UserDetailUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Aspect
public class DataFilterAspect {

    @Autowired
    private UserDetailUtil userDetailUtil;

    @Pointcut("@annotation(org.laokou.datasource.annotation.DataFilter)")
    public void dataFilterPointCut() {}

    @Before("dataFilterPointCut()")
    public void dataFilterPoint(JoinPoint point) {
        Object params = point.getArgs()[0];
        if (params != null && params instanceof BasePage) {
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            UserDetail userDetail = userDetailUtil.getUserDetail(request);
            //如果是超级管理员，不进行数据过滤
            if (userDetail.getSuperAdmin() == SuperAdminEnum.YES.ordinal()) {
                return;
            }
            try {
                //否则进行数据过滤
                BasePage page = (BasePage)params;
                String sqlFilter = getSqlFilter(userDetail, point);
                page.setSqlFilter(sqlFilter);
            }catch (Exception e){}
            return;
        }
        throw new CustomException(ErrorCode.SERVICE_MAINTENANCE);
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSqlFilter(UserDetail userDetail, JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        DataFilter dataFilter = method.getAnnotation(DataFilter.class);
        if (dataFilter == null) {
            dataFilter = AnnotationUtils.findAnnotation(method,DataFilter.class);
        }
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }
        StringBuilder sqlFilter = new StringBuilder();
        //用户列表
        if (CollectionUtils.isNotEmpty(userDetail.getDepts())) {
            List<Long> deptIds = userDetail.getDepts().stream().map(item -> item.getId()).collect(Collectors.toList());
            sqlFilter.append(" find_in_set(").append(tableAlias).append(dataFilter.deptId()).append(" , ").append("'").append(StringUtils.join(deptIds,",")).append("'").append(") or ");
        }
        sqlFilter.append(tableAlias).append(dataFilter.userId()).append(" = ").append("'").append(userDetail.getId()).append("' ");
        return sqlFilter.toString();
    }

}
