package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysResourceApplicationService;
import io.laokou.admin.interfaces.dto.SysResourceDTO;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.laokou.security.annotation.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:56
 */
@RestController
@Api(value = "音频管理API",protocols = "http",tags = "音频管理API")
@RequestMapping("/sys/resource/audio/api")
public class SysAudioApiController {

    @Autowired
    private SysResourceApplicationService sysResourceApplicationService;

    @PostMapping("/query")
    @ApiOperation("音频管理>查询")
    @PreAuthorize("sys:resource:audio:query")
    public HttpResultUtil<IPage<SysResourceVO>> query(@RequestBody SysResourceQO qo) {
        return new HttpResultUtil<IPage<SysResourceVO>>().ok(sysResourceApplicationService.queryResourcePage(qo));
    }

    @GetMapping(value = "/detail")
    @ApiOperation("音频管理>详情")
    public HttpResultUtil<SysResourceVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<SysResourceVO>().ok(sysResourceApplicationService.getResourceById(id));
    }

    @PostMapping(value = "/insert")
    @ApiOperation("音频管理>新增")
    @PreAuthorize("sys:resource:audio:insert")
    @OperateLog(module = "音频管理",name = "音频新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysResourceDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysResourceApplicationService.insertResource(dto,request));
    }

    @PutMapping(value = "/update")
    @ApiOperation("音频管理>修改")
    @PreAuthorize("sys:resource:audio:update")
    @OperateLog(module = "音频管理",name = "音频修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysResourceDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysResourceApplicationService.updateResource(dto,request));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation("音频管理>删除")
    @PreAuthorize("sys:resource:audio:delete")
    @OperateLog(module = "音频管理",name = "音频删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysResourceApplicationService.deleteResource(id));
    }

}
