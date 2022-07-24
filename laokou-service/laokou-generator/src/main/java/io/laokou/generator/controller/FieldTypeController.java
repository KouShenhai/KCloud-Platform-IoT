package io.laokou.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.generator.entity.FieldTypeEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.FieldTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;

/**
 * 字段类型管理
 *
 */
@RestController
@RequestMapping("gen/fieldtype")
@AllArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;

    @GetMapping("page")
    public HttpResultUtil<IPage<FieldTypeEntity>> page(BaseQO qo){
        return new HttpResultUtil<IPage<FieldTypeEntity>>().ok(fieldTypeService.page(qo));
    }

    @GetMapping("{id}")
    public HttpResultUtil<FieldTypeEntity> get(@PathVariable("id") Long id){
        return new HttpResultUtil<FieldTypeEntity>().ok(fieldTypeService.getById(id));
    }

    @GetMapping("list")
    public HttpResultUtil<Set<String>> list(){
        return new HttpResultUtil<Set<String>>().ok(fieldTypeService.getList());
    }

    @PostMapping
    public HttpResultUtil<Boolean> save(@RequestBody FieldTypeEntity entity){
        return new HttpResultUtil<Boolean>().ok(fieldTypeService.save(entity));
    }

    @PutMapping
    public HttpResultUtil<Boolean> update(@RequestBody FieldTypeEntity entity){
        return new HttpResultUtil<Boolean>().ok(fieldTypeService.updateById(entity));
    }

    @DeleteMapping
    public HttpResultUtil<Boolean> delete(@RequestBody Long[] ids){
        return new HttpResultUtil<Boolean>().ok(fieldTypeService.removeByIds(Arrays.asList(ids)));
    }
}