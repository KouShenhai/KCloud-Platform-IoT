package org.laokou.sys.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.laokou.common.i18n.dto.Result;
import org.laokou.sys.service.*;
import org.laokou.sys.entity.*;
import org.laokou.sys.vo.*;
import org.laokou.sys.qo.*;
import org.laokou.sys.dto.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.servlet.http.HttpServletResponse;
/**
 * <p>
 * 部门 接口
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@RestController
@Tag(name = "SysDeptAPI",description = "部门 API")
@RequestMapping("/sys/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService sysDeptService;

    @PostMapping(value = "/query")
    @Operation(summary = "查询部门",description = "查询部门")
    @PreAuthorize("hasAuthority('sys:dept:query')")
    public HttpResult<IPage<SysDeptVO>> query(@RequestBody SysDeptQo qo) {
        return new HttpResult().ok(sysDeptService.queryPageList(qo));
    }

    @PutMapping(value = "/save")
    @Operation(summary = "保存部门",description = "保存部门")
    @PreAuthorize("hasAuthority('sys:dept:save')")
    public HttpResult<Boolean> update(@RequestBody SysDeptDTO dto) {
        return new HttpResult().ok(sysDeptService.saveSysDept(dto));
    }

     @DeleteMapping(value = "/delete")
     @Operation(summary = "删除部门",description = "删除部门")
     @PreAuthorize("hasAuthority('sys:dept:delete')")
     public HttpResult<Boolean> delete(@RequestBody String[] ids) {
         return new HttpResult().ok(sysDeptService.removeByIds(Arrays.asList(ids)));
     }

      @GetMapping(value = "/detail")
      @Operation(summary = "查看部门",description = "查询部门")
      public HttpResult<SysDeptVO> detail(@RequestParam("id") String id) {

          SysDeptDO sysDictTypeDO = sysDeptService.getById(id);
          SysDeptVO vo = ConvertUtil.sourceToTarget(sysDictTypeDO, SysDeptVO.class);

          return new HttpResult().ok(vo);
      }

      @PostMapping(value = "/export")
      @Operation(summary = "导出部门",description = "导出部门")
      public void export(@RequestBody SysDeptQo qo, HttpServletResponse response) {

            sysDeptService.exportData(qo,response);
      }
}
