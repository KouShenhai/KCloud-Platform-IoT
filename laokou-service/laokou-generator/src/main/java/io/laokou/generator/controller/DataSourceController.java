package io.laokou.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.generator.config.DataSourceInfo;
import io.laokou.generator.entity.DataSourceEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.DataSourceService;
import io.laokou.generator.utils.DbUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * 数据源管理
 *

 */
@RestController
@RequestMapping("gen/datasource")
@AllArgsConstructor
public class DataSourceController {
    private final DataSourceService datasourceService;

    @GetMapping("page")
    public HttpResultUtil<IPage<DataSourceEntity>> page(BaseQO query){
        return new HttpResultUtil<IPage<DataSourceEntity>>().ok(datasourceService.page(query));
    }

    @GetMapping("list")
    public HttpResultUtil<List<DataSourceEntity>> list(){
        return new HttpResultUtil<List<DataSourceEntity>>().ok(datasourceService.getList());
    }

    @GetMapping("{id}")
    public HttpResultUtil<DataSourceEntity> get(@PathVariable("id") Long id){
        return new HttpResultUtil<DataSourceEntity>().ok(datasourceService.getById(id));
    }

    @GetMapping("test/{id}")
    public HttpResultUtil<String> test(@PathVariable("id") Long id){
        try {
            DbUtil.getConnection(new DataSourceInfo(datasourceService.getById(id)));
            return new HttpResultUtil<String>().ok("连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResultUtil<String>().error("连接失败，请检查配置信息");
        }
    }

    @PostMapping
    public HttpResultUtil<Boolean> save(@RequestBody DataSourceEntity entity){
        return new HttpResultUtil<Boolean>().ok(datasourceService.save(entity));
    }

    @PutMapping
    public HttpResultUtil<Boolean> update(@RequestBody DataSourceEntity entity){
        return new HttpResultUtil<Boolean>().ok(datasourceService.updateById(entity));
    }

    @DeleteMapping
    public HttpResultUtil<Boolean> delete(@RequestBody Long[] ids){
        return new HttpResultUtil<Boolean>().ok(datasourceService.removeByIds(Arrays.asList(ids)));
    }
}