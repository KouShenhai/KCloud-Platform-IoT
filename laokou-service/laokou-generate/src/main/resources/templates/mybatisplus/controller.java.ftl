package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.laokou.common.i18n.dto.Result;
import ${package.Service}.*;
import ${package.Entity}.*;
import ${package.Entity?replace("entity","vo")}.*;
import ${package.Entity?replace("entity","qo")}.*;
import ${package.Entity?replace("entity","dto")}.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.servlet.http.HttpServletResponse;
/**
 * <p>
 * ${table.comment!} 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@Tag(name = "${entity?substring(0,entity?length-2)}API",description = "${table.comment!} API")
@RequestMapping("/${package.ModuleName}/${entity?substring(0,entity?length-2)?uncap_first?replace(package.ModuleName,"")?uncap_first}")
@RequiredArgsConstructor
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${(table.serviceName)?uncap_first};

    @PostMapping(value = "/query")
    @Operation(summary = "查询${table.comment!}",description = "查询${table.comment!}")
    @PreAuthorize("hasAuthority('${package.ModuleName}:${entity?substring(0,entity?length-2)?uncap_first?replace(package.ModuleName,"")?uncap_first}:query')")
    public HttpResult<IPage<${entity?substring(0,entity?length-2)}VO>> query(@RequestBody ${entity?substring(0,entity?length-2)}Qo qo) {
        return new HttpResult().ok(${(table.serviceName)?uncap_first}.queryPageList(qo));
    }

    @PutMapping(value = "/save")
    @Operation(summary = "保存${table.comment!}",description = "保存${table.comment!}")
    @PreAuthorize("hasAuthority('${package.ModuleName}:${entity?substring(0,entity?length-2)?uncap_first?replace(package.ModuleName,"")?uncap_first}:save')")
    public HttpResult<Boolean> update(@RequestBody ${entity?substring(0,entity?length-2)}DTO dto) {
        return new HttpResult().ok(${(table.serviceName)?uncap_first}.save${entity?substring(0,entity?length-2)}(dto));
    }

     @DeleteMapping(value = "/delete")
     @Operation(summary = "删除${table.comment!}",description = "删除${table.comment!}")
     @PreAuthorize("hasAuthority('${package.ModuleName}:${entity?substring(0,entity?length-2)?uncap_first?replace(package.ModuleName,"")?uncap_first}:delete')")
     public HttpResult<Boolean> delete(@RequestBody String[] ids) {
         return new HttpResult().ok(${(table.serviceName)?uncap_first}.removeByIds(Arrays.asList(ids)));
     }

      @GetMapping(value = "/detail")
      @Operation(summary = "查看${table.comment!}",description = "查询${table.comment!}")
      public HttpResult<${entity?substring(0,entity?length-2)}VO> detail(@RequestParam("id") String id) {

          ${entity?substring(0,entity?length-2)}DO sysDictTypeDO = ${(table.serviceName)?uncap_first}.getById(id);
          ${entity?substring(0,entity?length-2)}VO vo = ConvertUtil.sourceToTarget(sysDictTypeDO, ${entity?substring(0,entity?length-2)}VO.class);

          return new HttpResult().ok(vo);
      }

      @PostMapping(value = "/export")
      @Operation(summary = "导出${table.comment!}",description = "导出${table.comment!}")
      public void export(@RequestBody ${entity?substring(0,entity?length-2)}Qo qo, HttpServletResponse response) {

            ${(table.serviceName)?uncap_first}.exportData(qo,response);
      }
}
</#if>
