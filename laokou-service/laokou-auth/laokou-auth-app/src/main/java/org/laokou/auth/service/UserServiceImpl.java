package org.laokou.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.auth.common.Constant.*;
import static org.laokou.auth.common.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final DeptGateway deptGateway;
    private final MenuGateway menuGateway;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 默认租户查询
        String encryptName = AesUtil.encrypt(username);
        User user = userGateway.getUserByUsername(encryptName,DEFAULT_TENANT,AUTH_PASSWORD);
        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        if (user == null) {
            throw new UsernameNotFoundException(MessageUtil.getMessage(USERNAME_PASSWORD_ERROR));
        }
        String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
        String clientPassword = user.getPassword();
        if (!passwordEncoder.matches(password, clientPassword)) {
            throw new UsernameNotFoundException(MessageUtil.getMessage(USERNAME_PASSWORD_ERROR));
        }
        // 是否锁定
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException(MessageUtil.getMessage(USERNAME_DISABLE));
        }
        Long userId = user.getId();
        Integer superAdmin = user.getSuperAdmin();
        // 权限标识列表
        List<String> permissionsList = menuGateway.getPermissions(userId,DEFAULT_TENANT,superAdmin);
        if (CollectionUtil.isEmpty(permissionsList)) {
            throw new UsernameNotFoundException(MessageUtil.getMessage(USERNAME_NOT_PERMISSION));
        }
        List<Long> deptIds = deptGateway.getDeptIds(userId,DEFAULT_TENANT,superAdmin);
        user.setDeptIds(deptIds);
        user.setPermissionList(permissionsList);
        // 登录IP
        user.setLoginIp(IpUtil.getIpAddr(request));
        // 登录时间
        user.setLoginDate(DateUtil.now());
        // 默认数据库
        user.setSourceName(DEFAULT_SOURCE);
        return user;
    }
}
