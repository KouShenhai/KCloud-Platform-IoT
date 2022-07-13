package io.laokou.admin.interfaces.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.WXMpAccountDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
/**
 * 公众号账号管理
 * @author limingze
 * @create: 2022-07-13 11:25
 * @Version 1.0
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/mp/account/api")
@Api(tags="公众号账号API",value = "公众号账号API",protocols = "http")
public class WXMpAccountApiController {

    @GetMapping("/query")
    @ApiOperation("公众号账号>查询")
    public HttpResultUtil<IPage<Object>> query(){
        return new HttpResultUtil<IPage<Object>>().ok(null);
    }

    @GetMapping("/detail")
    @ApiOperation("公众号账号>详情")
    public HttpResultUtil<Object> detail(){
        return new HttpResultUtil<Object>().ok(null);
    }

    @PostMapping("/insert")
    @ApiOperation("公众号账号>新增")
    public HttpResultUtil<Boolean> insert(@RequestBody WXMpAccountDTO dto){
        return new HttpResultUtil<Boolean>().ok(true);
    }

    @PutMapping("/update")
    @ApiOperation("公众号账号>修改")
    public HttpResultUtil<Boolean> update(@RequestBody WXMpAccountDTO dto){
        return new HttpResultUtil<Boolean>().ok(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("公众号账号>删除")
    public HttpResultUtil<Boolean> delete(){
        return new HttpResultUtil<Boolean>().ok(true);
    }

}