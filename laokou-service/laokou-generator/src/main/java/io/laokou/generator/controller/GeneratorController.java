package io.laokou.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.generator.config.DataSourceInfo;
import io.laokou.generator.entity.TableFieldEntity;
import io.laokou.generator.entity.TableInfoEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.GeneratorService;
import io.laokou.generator.service.TableFieldService;
import io.laokou.generator.service.TableInfoService;
import io.laokou.generator.utils.DbUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;
/**
 * 代码生成
 *
 */
@RestController
@RequestMapping("gen")
@AllArgsConstructor
public class GeneratorController {
    private final GeneratorService generatorService;
    private final TableInfoService tableInfoService;
    private final TableFieldService tableFieldService;

    @GetMapping("table/page")
    public HttpResultUtil<IPage<TableInfoEntity>> pageTable(BaseQO qo){
        IPage<TableInfoEntity> page = tableInfoService.page(qo);

        return new HttpResultUtil<IPage<TableInfoEntity>>().ok(page);
    }

    @GetMapping("table/{id}")
    public HttpResultUtil<TableInfoEntity> getTable(@PathVariable("id") Long id){
        TableInfoEntity table = tableInfoService.getById(id);

        List<TableFieldEntity> fieldList = tableFieldService.getByTableName(table.getTableName());
        table.setFields(fieldList);

        return new HttpResultUtil<TableInfoEntity>().ok(table);
    }

    @PutMapping("table")
    public HttpResultUtil<Boolean> updateTable(@RequestBody TableInfoEntity tableInfo){
        return new HttpResultUtil<Boolean>().ok(tableInfoService.updateById(tableInfo));
    }

    @DeleteMapping("table")
    public HttpResultUtil<Boolean> deleteTable(@RequestBody Long[] ids){
        tableInfoService.deleteBatchIds(ids);
        return new HttpResultUtil<Boolean>().ok(true);
    }

    /**
     * 获取数据源中所有表
     */
    @GetMapping("datasource/table/list/{id}")
    public HttpResultUtil<List<TableInfoEntity>> getDataSourceTableList(@PathVariable("id") Long id){
        try {
            //初始化配置信息
            DataSourceInfo info = generatorService.getDataSourceInfo(id);
            List<TableInfoEntity> tableInfoList = DbUtil.getTablesInfoList(info);

            return new HttpResultUtil<List<TableInfoEntity>>().ok(tableInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResultUtil<List<TableInfoEntity>>().error("数据源配置错误，请检查数据源配置！");
        }
    }

    /**
     * 导入数据源中的表
     */
    @PostMapping("datasource/table")
    public HttpResultUtil<Boolean> datasourceTable(@RequestBody TableInfoEntity tableInfo) {
        generatorService.datasourceTable(tableInfo);
        return new HttpResultUtil<Boolean>().ok(true);
    }

    /**
     * 更新列数据
     */
    @PutMapping("table/field/{tableId}")
    public HttpResultUtil<Boolean> updateTableField(@PathVariable("tableId") Long tableId, @RequestBody List<TableFieldEntity> tableFieldList) {
        generatorService.updateTableField(tableId, tableFieldList);
        return new HttpResultUtil<Boolean>().ok(true);
    }

    /**
     * 生成代码
     */
    @PostMapping("generator")
    public HttpResultUtil<Boolean> generator(@RequestBody TableInfoEntity tableInfo) throws Exception {
        //保存表信息
        tableInfoService.updateById(tableInfo);

        //生成代码
        generatorService.generatorCode(tableInfo);

        return new HttpResultUtil<Boolean>().ok(true);
    }
}