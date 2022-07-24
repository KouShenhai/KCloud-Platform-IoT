package ${package}<#if moduleName??>.${moduleName}</#if>.controller<#if subModuleName??>.${subModuleName}</#if>;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import ${package}.framework.common.page.PageResult;
import ${package}.framework.common.utils.Result;
import ${package}<#if moduleName??>.${moduleName}</#if>.convert<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Convert;
import ${package}<#if moduleName??>.${moduleName}</#if>.entity<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Entity;
import ${package}<#if moduleName??>.${moduleName}</#if>.service<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Service;
import ${package}<#if moduleName??>.${moduleName}</#if>.query<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Query;
import ${package}<#if moduleName??>.${moduleName}</#if>.vo<#if subModuleName??>.${subModuleName}</#if>.${ClassName}VO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@RestController
@RequestMapping("<#if moduleName??>${moduleName}/</#if>${classname}")
@Tag(name="${tableComment}")
@AllArgsConstructor
public class ${ClassName}Controller {
    private final ${ClassName}Service ${className}Service;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('<#if moduleName??>${moduleName}:</#if>${classname}:page')")
    public HttpResultUtil<PageHttpResultUtil<${ClassName}VO>> page(@Valid ${ClassName}Query query){
        PageHttpResultUtil<${ClassName}VO> page = ${className}Service.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('<#if moduleName??>${moduleName}:</#if>${classname}:info')")
    public HttpResultUtil<${ClassName}VO> get(@PathVariable("id") Long id){
        ${ClassName}Entity entity = ${className}Service.getById(id);

        return Result.ok(${ClassName}Convert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('<#if moduleName??>${moduleName}:</#if>${classname}:save')")
    public HttpResultUtil<String> save(@RequestBody ${ClassName}VO vo){
        ${className}Service.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('<#if moduleName??>${moduleName}:</#if>${classname}:update')")
    public HttpResultUtil<String> update(@RequestBody @Valid ${ClassName}VO vo){
        ${className}Service.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('<#if moduleName??>${moduleName}:</#if>${classname}:delete')")
    public HttpResultUtil<String> delete(@RequestBody List<Long> idList){
        ${className}Service.delete(idList);

        return Result.ok();
    }
}