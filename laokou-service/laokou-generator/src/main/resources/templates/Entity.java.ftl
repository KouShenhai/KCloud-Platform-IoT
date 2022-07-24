package ${package}<#if moduleName??>.${moduleName}</#if>.entity<#if subModuleName??>.${subModuleName}</#if>;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
<#list imports as i>
import ${i!};
</#list>
<#if baseClassEntity??>
import ${baseClassEntity.packageName};
</#if>

/**
 * ${tableComment}
 *
 * @author ${author} ${email}
 * @since ${version} ${date}
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("${tableName}")
public class ${ClassName}Entity<#if baseClassEntity??> extends ${baseClassEntity.code}</#if> {
<#list columnList as column>
	<#if baseClassEntity?? && baseClassEntity.fields?split(",")?seq_contains(column.columnName)>
    <#else>
	<#if column.columnComment!?length gt 0>
	/**
	* ${column.columnComment}
	*/
	</#if>
	<#if "deleted"?split(",")?seq_contains(column.columnName)>
	@TableLogic
	</#if>
    <#if "creator,create_time,org_id,deleted"?split(",")?seq_contains(column.columnName)>
	@TableField(fill = FieldFill.INSERT)
	</#if>
    <#if "updater,update_time"?split(",")?seq_contains(column.columnName)>
	@TableField(fill = FieldFill.INSERT_UPDATE)
	</#if>
    <#if column.pk>
	@TableId
	</#if>
	private ${column.attrType} ${column.attrName};

	</#if>
</#list>
}