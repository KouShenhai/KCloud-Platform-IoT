package io.laokou.generator.controller;
import cn.hutool.core.convert.Convert;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.generator.entity.GenTable;
import io.laokou.generator.entity.GenTableColumn;
import io.laokou.generator.service.GenTableColumnService;
import io.laokou.generator.service.GenTableService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * 代码生成 操作处理
 * 
 * @author ruoyi
 */
@RestController
public class GenController
{
    @Autowired
    private GenTableService genTableService;

    @Autowired
    private GenTableColumnService genTableColumnService;

    /**
     * 查询代码生成列表
     */
//    @GetMapping("/list")
//    public Object genList(GenTable genTable)
//    {
//        List<GenTable> list = genTableService.selectGenTableList(genTable);
//        return getDataTable(list);
//    }
    
    /**
     * 修改代码生成业务
     */
//    @GetMapping("/get/{tableId}")
//    public HttpResultUtil get(@PathVariable("tableId") Long tableId)
//    {
//        GenTable table = genTableService.selectGenTableById(tableId);
//        return R.data(table);
//    }
//
//    /**
//     * 查询数据库列表
//     */
//    @GetMapping("/db/list")
//    public HttpResultUtil dataList(GenTable genTable)
//    {
//        List<GenTable> list = genTableService.selectDbTableList(genTable);
//        return result(list);
//    }
//
//    /**
//     * 查询数据表字段列表
//     */
//    @GetMapping("edit")
//    public HttpResultUtil edit(GenTableColumn genTableColumn)
//    {
//        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(genTableColumn);
//        GenTable table = genTableService.selectGenTableById(genTableColumn.getTableId());
//        return R.data(table).put("rows",list).put("total", list.size());
//    }
//
//    /**
//     * 导入表结构（保存）
//     */
//    @PostMapping("/importTable")
//    public HttpResultUtil importTableSave(String tables)
//    {
//        String[] tableNames = Convert.toStrArray(tables);
//        // 查询表信息
//        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
//        String operName = getLoginName();
//        genTableService.importGenTable(tableList, operName);
//        return R.ok();
//    }
//
//    /**
//     * 修改保存代码生成业务
//     */
//    @PostMapping("/edit")
//    public HttpResultUtil editSave(@RequestBody @Validated GenTable genTable)
//    {
//        genTableService.validateEdit(genTable);
//        genTableService.updateGenTable(genTable);
//        return R.ok();
//    }
//
//    @PostMapping("/remove")
//    public HttpResultUtil remove(String ids)
//    {
//        genTableService.deleteGenTableByIds(ids);
//        return new HttpResultUtil().ok(true);
//    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    public HttpResultUtil preview(@PathVariable("tableId") Long tableId) throws Exception
    {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return new HttpResultUtil().ok(dataMap);
    }

    /**
     * 生成代码
     */
    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws Exception
    {
        byte[] data = genTableService.generatorCode(tableName);
        genCode(response, data);
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws Exception
    {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.generatorCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=ruoyi.zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}