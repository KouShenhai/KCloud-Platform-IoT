package io.laokou.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.generator.entity.BaseClassEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.BaseClassService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 基类管理
 *
 */
@RestController
@RequestMapping("gen/baseclass")
@AllArgsConstructor
public class BaseClassController {
    private final BaseClassService baseClassService;

    @GetMapping("page")
    public HttpResultUtil<IPage<BaseClassEntity>> page(BaseQO query){
        return new HttpResultUtil<IPage<BaseClassEntity>>().ok(baseClassService.page(query));
    }

    @GetMapping("list")
    public HttpResultUtil<List<BaseClassEntity>> list(){
        return new HttpResultUtil<List<BaseClassEntity>>().ok(baseClassService.getList());
    }

    @GetMapping("{id}")
    public HttpResultUtil<BaseClassEntity> get(@PathVariable("id") Long id){
        return new HttpResultUtil<BaseClassEntity>().ok(baseClassService.getById(id));
    }

    @PostMapping
    public HttpResultUtil<Boolean> save(@RequestBody BaseClassEntity entity){
        return new HttpResultUtil<Boolean>().ok(baseClassService.save(entity));
    }

    @PutMapping
    public HttpResultUtil<Boolean> update(@RequestBody BaseClassEntity entity){
        return new HttpResultUtil<Boolean>().ok(baseClassService.updateById(entity));
    }

    @DeleteMapping
    public HttpResultUtil<Boolean> delete(@RequestBody Long[] ids){
        return new HttpResultUtil<Boolean>().ok(baseClassService.removeByIds(Arrays.asList(ids)));
    }
}