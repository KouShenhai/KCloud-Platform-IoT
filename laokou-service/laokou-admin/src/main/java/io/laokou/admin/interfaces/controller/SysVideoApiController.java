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
@Api(value = "视频管理API",protocols = "http",tags = "视频管理API")
@RequestMapping("/sys/resource/video/api")
public class SysVideoApiController {

    @Autowired
    private SysResourceApplicationService sysResourceApplicationService;

    @PostMapping("/query")
    @ApiOperation("视频管理>查询")
    @PreAuthorize("sys:resource:video:query")
    public HttpResultUtil<IPage<SysResourceVO>> query(@RequestBody SysResourceQO qo) {
        return new HttpResultUtil<IPage<SysResourceVO>>().ok(sysResourceApplicationService.queryResourcePage(qo));
    }

    @GetMapping(value = "/detail")
    @ApiOperation("视频管理>详情")
    public HttpResultUtil<SysResourceVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<SysResourceVO>().ok(sysResourceApplicationService.getResourceById(id));
    }

    @PostMapping(value = "/insert")
    @ApiOperation("视频管理>新增")
    @PreAuthorize("sys:resource:video:insert")
    @OperateLog(module = "视频管理",name = "视频新增")
    public HttpResultUtil<Boolean> insert(@RequestBody SysResourceDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysResourceApplicationService.insertResource(dto,request));
    }

    @PutMapping(value = "/update")
    @ApiOperation("视频管理>修改")
    @PreAuthorize("sys:resource:video:update")
    @OperateLog(module = "视频管理",name = "视频修改")
    public HttpResultUtil<Boolean> update(@RequestBody SysResourceDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysResourceApplicationService.updateResource(dto,request));
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation("视频管理>删除")
    @PreAuthorize("sys:resource:video:delete")
    @OperateLog(module = "视频管理",name = "视频删除")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysResourceApplicationService.deleteResource(id));
    }

}
