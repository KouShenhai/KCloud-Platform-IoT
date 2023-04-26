package com.dw.admin.module.${moduleName}.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dw.admin.module.${moduleName}.dto.${className}DTO;
import com.dw.admin.module.${moduleName}.entity.${className};
import com.dw.admin.module.${moduleName}.qo.${className}Qo;
import com.dw.admin.module.${moduleName}.vo.${className}VO;
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