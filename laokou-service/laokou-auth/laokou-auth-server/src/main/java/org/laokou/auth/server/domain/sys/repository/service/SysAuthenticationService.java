package org.laokou.auth.server.domain.sys.repository.service;

/**
 * @author laokou
 */
public interface SysAuthenticationService {
    /**
     * 获取token
     * @param loginName
     * @param accessToken
     * @return
     */
    String getAccessToken(String loginName,String accessToken);
}
