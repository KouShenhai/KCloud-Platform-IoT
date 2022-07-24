package ${package}<#if moduleName??>.${moduleName}</#if>.vo<#if subModuleName??>.${subModuleName}</#if>;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import ${package}.framework.common.utils.DateUtils;
<#list imports as i>
import ${i!};
</#list>

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Data
@Schema(description = "${tableComment}")
public class ${ClassName}VO implements Serializable {
	private static final long serialVersionUID = 1L;

<#list columnList as column>
	<#if column.columnComment!?length gt 0>
	@Schema(description = "${column.columnComment}")
	</#if>
	<#if column.attrType == 'Date'>
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	</#if>
	private ${column.attrType} ${column.attrName};

</#list>

}