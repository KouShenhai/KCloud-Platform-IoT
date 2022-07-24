package ${package}<#if moduleName??>.${moduleName}</#if>.convert<#if subModuleName??>.${subModuleName}</#if>;

import ${package}<#if moduleName??>.${moduleName}</#if>.entity<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Entity;
import ${package}<#if moduleName??>.${moduleName}</#if>.vo<#if subModuleName??>.${subModuleName}</#if>.${ClassName}VO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Mapper
public interface ${ClassName}Convert {
    ${ClassName}Convert INSTANCE = Mappers.getMapper(${ClassName}Convert.class);

    ${ClassName}Entity convert(${ClassName}VO vo);

    ${ClassName}VO convert(${ClassName}Entity entity);

    List<${ClassName}VO> convertList(List<${ClassName}Entity> list);

}