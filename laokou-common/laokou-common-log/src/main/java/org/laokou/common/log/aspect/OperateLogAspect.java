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
package org.laokou.common.log.aspect;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.enums.ResultStatusEnum;
import org.laokou.common.core.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.dto.OperateLogDTO;
import org.laokou.common.log.enums.DataTypeEnum;
import org.laokou.common.log.service.SysOperateLogService;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class OperateLogAspect {

    private final SysOperateLogService sysOperateLogService;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(org.laokou.common.log.annotation.OperateLog)")
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

    @Async
    @Transactional(rollbackFor = Exception.class)
    protected void handleLog(final JoinPoint joinPoint,final Exception e) throws IOException {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        //获取注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        OperateLog operateLog = method.getAnnotation(OperateLog.class);
        if (operateLog == null) {
            operateLog = AnnotationUtils.findAnnotation(method, OperateLog.class);
        }
        String ip = IpUtil.getIpAddr(request);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        List<?> params = new ArrayList<>(Arrays.asList(args)).stream().filter(arg -> (!(arg instanceof HttpServletRequest)
                && !(arg instanceof HttpServletResponse))).collect(Collectors.toList());
        OperateLogDTO dto = new OperateLogDTO();
        assert operateLog != null;
        dto.setModule(operateLog.module());
        dto.setOperation(operateLog.name());
        dto.setRequestUri(request.getRequestURI());
        dto.setRequestIp(ip);
        dto.setRequestAddress(AddressUtil.getRealAddress(ip));
        dto.setOperator(UserUtil.getUsername());
        dto.setCreator(UserUtil.getUserId());
        dto.setDeptId(UserUtil.getDeptId());
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
            dto.setRequestParams(JacksonUtil.toJsonStr(params, true));
        }
        dto.setTenantId(UserUtil.getTenantId());
        sysOperateLogService.insertOperateLog(dto);
    }

}
