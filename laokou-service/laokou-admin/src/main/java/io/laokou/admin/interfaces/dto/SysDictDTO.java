package io.laokou.admin.interfaces.dto;
import lombok.Data;
@Data
public class SysDictDTO {

    private Long id;
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
    /**
     * 排序
     */
    private Integer sort;
}
