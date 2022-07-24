package ${package}<#if moduleName??>.${moduleName}</#if>.query<#if subModuleName??>.${subModuleName}</#if>;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ${package}.framework.common.query.Query;

<#list imports as i>
import ${i!};
</#list>

/**
* ${tableComment}查询
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "${tableComment}查询")
public class ${ClassName}Query extends Query {
<#list columnList as column>
<#if column.query>
    <#if column.columnComment!?length gt 0>
    @Schema(description = "${column.columnComment}")
    </#if>
    private ${column.attrType} ${column.attrName};

</#if>
</#list>
}