import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${groupId}.admin.module.${moduleName}.dto.${className}DTO;
import ${groupId}.admin.module.${moduleName}.entity.${className};
import ${groupId}.admin.module.${moduleName}.mapper.${className}Mapper;
import ${groupId}.admin.module.${moduleName}.qo.${className}Qo;
import ${groupId}.admin.module.${moduleName}.service.${className}Service;
import ${groupId}.admin.module.${moduleName}.vo.${className}VO;
import ${groupId}.common.core.exception.SysException;
import ${groupId}.common.core.utils.ConvertUtil;
import ${groupId}.common.core.utils.DateUtil;
import ${groupId}.common.mybatisplus.utils.IdUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Date;
/**
 * @author ${author}
 */
@Service
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements ${className}Service {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(${className}DTO dto) {
        ${className} ${moduleName} = ConvertUtil.sourceToTarget(dto, ${className}.class);
        ${moduleName}.setCreateBy("admin");
        ${moduleName}.setCreateTime(new Date());
        ${moduleName}.setId(IdUtil.defaultStrId());
        return this.save(${moduleName});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${className}DTO dto) {
        String id = dto.getId();
        if (StringUtils.isBlank(id)) {
            throw new SysException("ID不为空");
        }
        ${className} ${moduleName} = ConvertUtil.sourceToTarget(dto, ${className}.class);
        ${moduleName}.setUpdateBy("admin");
        ${moduleName}.setUpdateTime(new Date());
        return this.updateById(${moduleName});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String[] ids) {
        return this.removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public ${className}VO get(String id) {
        ${className} ${moduleName} = this.getById(id);
        if (${moduleName} == null) {
            throw new SysException("职工信息不存在，请重新录入");
        }
        return ConvertUtil.sourceToTarget(${moduleName},${className}.class);
    }

    @Override
    public IPage<${moduleName}VO> query(${className}Qo qo) {
        Page<${className}VO> page = new Page<>(qo.getPageNo(), qo.getPageSize());
        return this.baseMapper.query(page,qo);
    }
}
