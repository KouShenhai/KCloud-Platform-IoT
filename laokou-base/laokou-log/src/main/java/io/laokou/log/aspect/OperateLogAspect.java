package io.laokou.log.aspect;
import com.alibaba.fastjson.JSON;
import io.laokou.common.constant.Constant;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.enums.DataTypeEnum;
import io.laokou.common.enums.ResultStatusEnum;
import io.laokou.common.utils.AddressUtil;
import io.laokou.common.utils.HttpContextUtil;
import io.laokou.common.utils.IpUtil;
import io.laokou.common.utils.SpringContextUtil;
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
import org.assertj.core.util.Lists;
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
        dto.setOperator(request.getHeader(Constant.USERNAME_HEAD));
        dto.setCreator(Long.valueOf(request.getHeader(Constant.USER_KEY_HEAD)));
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
            dto.setRequestParams(JSON.toJSONString(params, true));
        }
        //发布事件
        SpringContextUtil.publishEvent(new OperateLogEvent(dto));
    }

}
