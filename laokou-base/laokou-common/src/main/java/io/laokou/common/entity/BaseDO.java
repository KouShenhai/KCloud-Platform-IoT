package io.laokou.common.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
/**
 * 基础实体类，所有实体都需要继承
 * @author Kou Shenhai
 */
@Data
public abstract class BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 修改者
     */
    private Long editor;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 是否删除
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer delFlag;

}
