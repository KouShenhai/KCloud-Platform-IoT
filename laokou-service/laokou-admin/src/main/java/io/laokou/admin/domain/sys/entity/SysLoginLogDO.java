package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@TableName("boot_sys_login_log")
@ApiModel("系统日志>登录日志DO")
public class SysLoginLogDO extends BaseDO {

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
