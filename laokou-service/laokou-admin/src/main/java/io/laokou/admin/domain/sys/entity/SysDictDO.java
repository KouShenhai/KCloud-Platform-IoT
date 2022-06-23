package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典管理
 * @author  Kou Shenhai
 */
@Data
@TableName("boot_sys_dict")
public class SysDictDO extends BaseDO implements Serializable {

    /**
     * 标签
     */
    private String dictLabel;
    /**
     * 类型
     */
    private String type;
    /**
     * 值
     */
    private String dictValue;
    /**
     * 状态 0 正常 1 停用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
