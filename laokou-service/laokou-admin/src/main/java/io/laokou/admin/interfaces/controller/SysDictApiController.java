package io.laokou.admin.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.application.service.SysDictApplicationService;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
