package ${package}<#if moduleName??>.${moduleName}</#if>.service<#if subModuleName??>.${subModuleName}</#if>;

import ${package}.framework.common.page.PageResult;
import ${package}.framework.common.service.BaseService;
import ${package}<#if moduleName??>.${moduleName}</#if>.vo<#if subModuleName??>.${subModuleName}</#if>.${ClassName}VO;
import ${package}<#if moduleName??>.${moduleName}</#if>.query<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Query;
import ${package}<#if moduleName??>.${moduleName}</#if>.entity<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Entity;

import java.util.List;

/**
 * ${tableComment}
 *
 * @author ${author} ${email}
 * @since ${version} ${date}
 */
public interface ${ClassName}Service extends BaseService<${ClassName}Entity> {

    PageHttpResultUtil<${ClassName}VO> page(${ClassName}Query query);

    void save(${ClassName}VO vo);

    void update(${ClassName}VO vo);

    void delete(List<Long> idList);
}