package io.laokou.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 字段类型管理
 *

 */
@Data
@TableName("gen_field_type")
public class FieldTypeEntity {
	/**
	 * id
	 */
	@TableId
	private Long id;
    /**
     * 字段类型
     */
	private String columnType;
    /**
     * 属性类型
     */
	private String attrType;
	/**
	 * 属性包名
	 */
	private String packageName;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
}