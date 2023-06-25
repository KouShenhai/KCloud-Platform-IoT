package  ${package.Entity?replace("entity","dto")};
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
/**
 * @Description ${table.comment}
 * @Author ${author}
 * @Date ${date}
 */
@Data
public class ${entity?substring(0,entity?length-2)}DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "id",description = "主键")
    private String id;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
    /**
    * ${field.comment}
    */
    </#if>
    <#if springdoc>
    @Schema(name = "${field.propertyName}",description = "${field.comment}")
    <#elseif swagger>
    @ApiModelProperty("${field.comment}")
    <#else>

    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

}
