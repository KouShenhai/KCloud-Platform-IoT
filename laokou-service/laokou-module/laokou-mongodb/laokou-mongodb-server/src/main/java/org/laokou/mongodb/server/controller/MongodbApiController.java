package org.laokou.mongodb.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.mongodb.client.model.MongodbModel;
import org.laokou.mongodb.client.vo.SearchVO;
import org.laokou.mongodb.server.form.QueryForm;
import org.laokou.mongodb.server.utils.MongodbFieldUtil;
import org.laokou.mongodb.server.utils.MongodbUtil;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Mongodb API",description = "统计报表API")
public class MongodbApiController {

    private final MongodbUtil mongodbUtil;

    @PostMapping("/insert")
    @Operation(summary = "统计报表>新增",description = "统计报表>新增")
    public void insert(@RequestBody MongodbModel mongodbModel) {
        final String collectionName = mongodbModel.getCollectionName();
        final String jsonData = mongodbModel.getData();
        final Object obj = MongodbFieldUtil.getObj(collectionName,jsonData);
        mongodbUtil.insert(collectionName,obj);
    }

    @PostMapping("/query")
    @Operation(summary = "统计报表>查询",description = "统计报表>查询")
    public HttpResult<SearchVO> query(@RequestBody final QueryForm queryForm) {
        HttpResult<SearchVO> result = new HttpResult<>();
        result.setData(mongodbUtil.query(queryForm));
        return result;
    }

    @PostMapping("/get")
    @Operation(summary = "统计报表>详情",description = "统计报表>详情")
    public HttpResult<Object> get(@RequestParam("collectionName")final String collectionName, @RequestParam("id")final String id) {
        HttpResult<Object> result = new HttpResult<>();
        final Class<?> clazz = MongodbFieldUtil.getClazz(collectionName);
        result.setData(mongodbUtil.get(clazz,id));
        return result;
    }

    @PostMapping("/insertBatch")
    @Operation(summary = "统计报表>批量新增",description = "统计报表>批量新增")
    public void insertBatch(@RequestBody MongodbModel mongodbModel) {
        final String collectionName = mongodbModel.getCollectionName();
        final String jsonData = mongodbModel.getData();
        final List<? extends Object> objList = MongodbFieldUtil.getObjList(collectionName,jsonData);
        mongodbUtil.insertBatch(collectionName, objList);
    }

    @DeleteMapping("/all")
    @Operation(summary = "统计报表>清空",description = "统计报表>清空")
    public void deleteAll(@RequestParam("collectionName")final String collectionName ) {
        mongodbUtil.deleteAll(collectionName);
    }

}
