package io.laokou.admin.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@TableName("boot_sys_operate_log")
@ApiModel("系统日志>操作日志DO")
public class SysOperateLogDO extends BaseDO {

    /**
     * 模块名称，如：系统菜单
     */
    private String module;

    /**
     * 操作名称
     */
    private String operation;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 浏览器版本
     */
    private String userAgent;

    /**
     * IP地址
     */
    private String requestIp;

    /**
     * 归属地
     */
    private String requestAddress;

    /**
     * 状态  0：成功   1：失败
     */
    private Integer requestStatus;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 方法名称
     */
    private String methodName;

}
