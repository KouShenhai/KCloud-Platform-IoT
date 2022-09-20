package io.laokou.auth.interfaces.vo;
import io.laokou.common.utils.TreeUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统菜单VO
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/1/16 0016 下午 9:08
 */
@Data
@ApiModel("系统菜单VO")
public class SysMenuVO extends TreeUtil.TreeNo<SysMenuVO> implements Serializable {

    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 资源URL
     */
    private String url;
    /**
     * 请求方式（如：GET、POST、PUT、DELETE）
     */
    private String method;
    /**
     * 认证等级   0：权限认证   1：登录认证    2：无需认证
     */
    private Integer authLevel;
    /**
     * 权限标识
     */
    private String permissions;

}
