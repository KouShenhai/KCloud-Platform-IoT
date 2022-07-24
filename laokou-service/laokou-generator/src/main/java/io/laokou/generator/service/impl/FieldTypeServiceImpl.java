package io.laokou.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.generator.dao.FieldTypeDao;
import io.laokou.generator.entity.FieldTypeEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.FieldTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 字段类型管理
 *

 */
@Service
public class FieldTypeServiceImpl extends ServiceImpl<FieldTypeDao, FieldTypeEntity> implements FieldTypeService {

    @Override
    public IPage<FieldTypeEntity> page(BaseQO query) {
        IPage<FieldTypeEntity> page = baseMapper.selectPage(
            getPage(query),
            getWrapper(query)
        );
        return page;
    }

    @Override
    public Map<String, FieldTypeEntity> getMap() {
        List<FieldTypeEntity> list = baseMapper.selectList(null);
        Map<String, FieldTypeEntity> map = new LinkedHashMap<>(list.size());
        for(FieldTypeEntity entity : list){
            map.put(entity.getColumnType().toLowerCase(), entity);
        }
        return map;
    }

    @Override
    public Set<String> getPackageListByTableId(Long tableId) {
        return baseMapper.getPackageListByTableId(tableId);
    }

    @Override
    public Set<String> getList() {
        return baseMapper.list();
    }

    protected IPage<FieldTypeEntity> getPage(BaseQO query) {
        Page<FieldTypeEntity> page = new Page(query.getPageNum(), query.getPageSize());
        page.addOrder(OrderItem.desc("id"));
        return page;
    }

    protected QueryWrapper<FieldTypeEntity> getWrapper(BaseQO query){
        QueryWrapper<FieldTypeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.isNotEmpty(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.isNotEmpty(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.isNotEmpty(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.isNotEmpty(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.isNotEmpty(query.getDbType()), "db_type", query.getDbType());
        return wrapper;
    }

}