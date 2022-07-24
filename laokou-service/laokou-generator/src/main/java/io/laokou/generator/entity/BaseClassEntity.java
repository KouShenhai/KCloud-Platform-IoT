package io.laokou.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 基类管理
 *

 */
@Data
@TableName("gen_base_class")
public class BaseClassEntity {
	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 基类包名
	 */
	private String packageName;
    /**
     * 基类编码
     */
	private String code;
    /**
     * 公共字段，多个用英文逗号分隔
     */
	private String fields;
    /**
     * 备注
     */
	private String remark;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
}