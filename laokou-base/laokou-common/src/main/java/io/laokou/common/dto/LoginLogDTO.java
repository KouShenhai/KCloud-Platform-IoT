package io.laokou.common.dto;
import lombok.Data;
@Data
public class LoginLogDTO {

    /**
     * 登录用户
     */
    private String loginName;

    /**
     * ip地址
     */
    private String requestIp;
    /**
     * 操作地点
     */
    private String requestAddress;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 状态  0：成功   1：失败
     */
    private Integer requestStatus;

    /**
     * 提示信息
     */
    private String msg;

}
