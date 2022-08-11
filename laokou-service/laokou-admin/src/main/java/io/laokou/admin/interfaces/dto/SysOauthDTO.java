package io.laokou.admin.interfaces.dto;

import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/11 0011 上午 9:43
 */
@Data
public class SysOauthDTO {

    private Long id;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 资源集合
     */
    private String resourceIds;

    /**
     * 应用密钥
     */
    private String clientSecret;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 授权类型
     */
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    private String webServerRedirectUri;

    /**
     * 权限
     */
    private String authorities;

    /**
     * 令牌秒数
     */
    private String accessTokenValidity;

    /**
     * 刷新秒数
     */
    private String refreshTokenValidity;

    /**
     * 附加说明
     */
    private String additionalInformation;

    /**
     * 自动授权
     */
    private String autoapprove;

    private Integer sort;

}
