package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysDictApplicationService;
import io.laokou.admin.interfaces.dto.DictDTO;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.annotation.OperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统字典控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "系统字典API",protocols = "http",tags = "系统字典API")
@RequestMapping("/sys/dict/api")
public class SysDictApiController {

    @Autowired
    private SysDictApplicationService sysDictApplicationService;

    @PostMapping(value = "/query")
    @CrossOrigin()
    @ApiOperation("系统字典>查询")
    public HttpResultUtil<IPage<DictVO>> query(@RequestBody DictQO qo) {
        return new HttpResultUtil<IPage<DictVO>>().ok(sysDictApplicationService.queryDictPage(qo));
    }

    @GetMapping(value = "/detail")
    @CrossOrigin()
    @ApiOperation("系统字典>详情")
    public HttpResultUtil<DictVO> detail(@RequestParam("id") Long id) {
        return new HttpResultUtil<DictVO>().ok(sysDictApplicationService.getDictById(id));
    }

    @PostMapping(value = "/insert")
    @CrossOrigin()
    @ApiOperation("系统字典>新增")
    @OperateLog(module = "系统字典",name = "新增字典")
    public HttpResultUtil<Boolean> insert(@RequestBody DictDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDictApplicationService.insertDict(dto,request));
    }

    @PutMapping(value = "/update")
    @CrossOrigin()
    @ApiOperation("系统字典>修改")
    @OperateLog(module = "系统字典",name = "修改字典")
    public HttpResultUtil<Boolean> update(@RequestBody DictDTO dto, HttpServletRequest request) {
        return new HttpResultUtil<Boolean>().ok(sysDictApplicationService.updateDict(dto,request));
    }

    @DeleteMapping(value = "/delete")
    @CrossOrigin()
    @ApiOperation("系统字典>删除")
    @OperateLog(module = "系统字典",name = "删除字典")
    public HttpResultUtil<Boolean> delete(@RequestParam("id") Long id) {
        return new HttpResultUtil<Boolean>().ok(sysDictApplicationService.deleteDict(id));
    }

}
