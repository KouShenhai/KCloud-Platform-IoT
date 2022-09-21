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
package io.laokou.log.aspect;
import com.google.common.collect.Lists;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.enums.DataTypeEnum;
import io.laokou.common.enums.ResultStatusEnum;
import io.laokou.common.user.SecurityUser;
import io.laokou.common.utils.*;
import io.laokou.log.annotation.OperateLog;
import io.laokou.log.event.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Aspect
@Slf4j
public class OperateLogAspect {

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(io.laokou.log.annotation.OperateLog)")
    public void logPointCut() {}

    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) throws IOException {
        handleLog(joinPoint,null);
    }

    @AfterThrowing(pointcut = "logPointCut()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Exception e) throws IOException {
        handleLog(joinPoint,e);
    }

    protected void handleLog(final JoinPoint joinPoint,final Exception e) throws IOException {
        //获取注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (null == method) {
            return;
        }
        OperateLog operateLog = method.getAnnotation(OperateLog.class);
        if (operateLog == null) {
            operateLog = AnnotationUtils.findAnnotation(method,OperateLog.class);
        }
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IpUtil.getIpAddr(request);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        List<?> params = Lists.newArrayList(Arrays.asList(args)).stream().filter(arg -> (!(arg instanceof HttpServletRequest)
                && !(arg instanceof HttpServletResponse))).collect(Collectors.toList());
        OperateLogDTO dto = new OperateLogDTO();
        dto.setModule(operateLog.module());
        dto.setOperation(operateLog.name());
        dto.setRequestUri(request.getRequestURI());
        dto.setRequestIp(ip);
        dto.setRequestAddress(AddressUtil.getRealAddress(ip));
        dto.setOperator(SecurityUser.getUsername(request));
        dto.setCreator(SecurityUser.getUserId(request));
        if (null != e) {
            dto.setRequestStatus(ResultStatusEnum.FAIL.ordinal());
            dto.setErrorMsg(e.getMessage());
        } else {
            dto.setRequestStatus(ResultStatusEnum.SUCCESS.ordinal());
        }
        dto.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        dto.setMethodName(className + "." + methodName + "()");
        dto.setRequestMethod(request.getMethod());
        if (DataTypeEnum.TEXT.equals(operateLog.type())) {
            dto.setRequestParams(JacksonUtil.toJsonStr(params,true));
        }
        //发布事件
        SpringContextUtil.publishEvent(new OperateLogEvent(dto));
    }

}
