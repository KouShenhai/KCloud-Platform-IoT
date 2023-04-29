import ${groupId}.module.${moduleName}.dto.${className}DTO;
import ${groupId}.module.${moduleName}.qo.${className}Qo;
import ${groupId}.admin.module.${moduleName}.service.${className}Service;
import ${groupId}.admin.module.${moduleName}.vo.${className}VO;
import ${groupId}.common.core.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author ${author}
 */
@RestController
@RequestMapping("/${moduleName}")
@RequiredArgsConstructor
@Api(value = "${moduleDesc}接口", tags = "${moduleDesc}接口")
public class ${className}Controller {

    private final ${className}Service ${moduleName}Service;

    @PostMapping("query")
    @ApiOperation(value = "查询${name}")
    public Result<IPage<${className}VO>> query(@RequestBody ${className}Qo qo) {
        return Result.success(${mouduleName}Service.query(qo));
    }

    @PostMapping()
    @ApiOperation(value = "新增${name}")
    public Result<Boolean> insert(@RequestBody ${className}DTO dto) {
        return Result.success(${moduleName}Service.insert(dto));
    }

    @PutMapping()
    @ApiOperation(value = "修改${name}")
    public Result<Boolean> update(@RequestBody ${className}DTO dto) {
        return Result.success(${moduleName}Service.update(dto));
    }

    @DeleteMapping()
    @ApiOperation(value = "删除${name}")
    public Result<Boolean> update(@RequestBody String[] ids) {
        return Result.success(${moduleName}Service.delete(ids));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "查看${name}")
    public Result<${className}VO> update(@PathVariable("id")String id) {
        return Result.success(${moduleName}Service.get(id));
    }
}