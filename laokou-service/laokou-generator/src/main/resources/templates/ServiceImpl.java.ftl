package ${package}<#if moduleName??>.${moduleName}</#if>.service<#if subModuleName??>.${subModuleName}</#if>.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import ${package}.framework.common.page.PageResult;
import ${package}.framework.common.service.impl.BaseServiceImpl;
import ${package}<#if moduleName??>.${moduleName}</#if>.convert<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Convert;
import ${package}<#if moduleName??>.${moduleName}</#if>.entity<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Entity;
import ${package}<#if moduleName??>.${moduleName}</#if>.query<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Query;
import ${package}<#if moduleName??>.${moduleName}</#if>.vo<#if subModuleName??>.${subModuleName}</#if>.${ClassName}VO;
import ${package}<#if moduleName??>.${moduleName}</#if>.dao<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Dao;
import ${package}<#if moduleName??>.${moduleName}</#if>.service<#if subModuleName??>.${subModuleName}</#if>.${ClassName}Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ${tableComment}
 *
 * @author ${author} ${email}
 * @since ${version} ${date}
 */
@Service
@AllArgsConstructor
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}Dao, ${ClassName}Entity> implements ${ClassName}Service {

    @Override
    public PageHttpResultUtil<${ClassName}VO> page(${ClassName}Query query) {
        IPage<${ClassName}Entity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageHttpResultUtil<>(${ClassName}Convert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private QueryWrapper<${ClassName}Entity> getWrapper(${ClassName}Query query){
        QueryWrapper<${ClassName}Entity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    @Override
    public void save(${ClassName}VO vo) {
        ${ClassName}Entity entity = ${ClassName}Convert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(${ClassName}VO vo) {
        ${ClassName}Entity entity = ${ClassName}Convert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}