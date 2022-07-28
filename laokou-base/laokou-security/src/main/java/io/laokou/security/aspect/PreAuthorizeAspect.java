package io.laokou.security.aspect;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpContextUtil;
import io.laokou.security.annotation.PreAuthorize;
import io.laokou.security.utils.UserDetailUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/18 0018 上午 9:52
 */
@Component
@Aspect
public class PreAuthorizeAspect {

    @Autowired
    private UserDetailUtil userDetailUtil;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(io.laokou.security.annotation.PreAuthorize)")
    public void authorizePointCut() {}

    @Around("authorizePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        final String ticket = request.getHeader(Constant.TICKET);
        //网关如果已经认证，则无需认证
        if (Constant.TICKET.equals(ticket)) {
            return point.proceed();
        }
        if (checkPermission(userDetailUtil.getUserDetail(request),point)) {
            return point.proceed();
        }
        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    private boolean checkPermission(UserDetail userDetail,JoinPoint point) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        final PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
        final String permission = preAuthorize.value();
        final List<String> permissionsList = userDetail.getPermissionsList();
        if (permissionsList.contains(permission)) {
            return true;
        }
        return false;
    }

}
