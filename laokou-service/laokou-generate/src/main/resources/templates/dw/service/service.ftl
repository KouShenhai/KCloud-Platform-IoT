import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${groupId}.admin.module.${moduleName}.dto.${className}DTO;
import ${groupId}.admin.module.${moduleName}.entity.${className};
import ${groupId}.admin.module.${moduleName}.qo.${className}Qo;
import ${groupId}.admin.module.${moduleName}.vo.${className}VO;
/**
 * @author ${author}
 */
public interface ${className}Service extends IService<${className}> {
    Boolean insert(${className}DTO dto);
    Boolean update(${className}DTO dto);
    Boolean delete(String[] ids);
    ${className}VO get(String id);
    IPage<${className}VO> query(${className}Qo qo);
}