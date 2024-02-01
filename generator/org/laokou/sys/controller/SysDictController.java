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
 * 字典 接口
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@RestController
@Tag(name = "SysDictAPI",description = "字典 API")
@RequestMapping("/sys/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictService sysDictService;

    @PostMapping(value = "/query")
    @Operation(summary = "查询字典",description = "查询字典")
    @PreAuthorize("hasAuthority('sys:dict:query')")
    public HttpResult<IPage<SysDictVO>> query(@RequestBody SysDictQo qo) {
        return new HttpResult().ok(sysDictService.queryPageList(qo));
    }

    @PutMapping(value = "/save")
    @Operation(summary = "保存字典",description = "保存字典")
    @PreAuthorize("hasAuthority('sys:dict:save')")
    public HttpResult<Boolean> update(@RequestBody SysDictDTO dto) {
        return new HttpResult().ok(sysDictService.saveSysDict(dto));
    }

     @DeleteMapping(value = "/delete")
     @Operation(summary = "删除字典",description = "删除字典")
     @PreAuthorize("hasAuthority('sys:dict:delete')")
     public HttpResult<Boolean> delete(@RequestBody String[] ids) {
         return new HttpResult().ok(sysDictService.removeByIds(Arrays.asList(ids)));
     }

      @GetMapping(value = "/detail")
      @Operation(summary = "查看字典",description = "查询字典")
      public HttpResult<SysDictVO> detail(@RequestParam("id") String id) {

          SysDictDO sysDictTypeDO = sysDictService.getById(id);
          SysDictVO vo = ConvertUtil.sourceToTarget(sysDictTypeDO, SysDictVO.class);

          return new HttpResult().ok(vo);
      }

      @PostMapping(value = "/export")
      @Operation(summary = "导出字典",description = "导出字典")
      public void export(@RequestBody SysDictQo qo, HttpServletResponse response) {

            sysDictService.exportData(qo,response);
      }
}
