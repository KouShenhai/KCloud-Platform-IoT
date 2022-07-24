package io.laokou.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.generator.dao.DataSourceDao;
import io.laokou.generator.entity.DataSourceEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.DataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 数据源管理
 *

 */
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceDao, DataSourceEntity> implements DataSourceService {

    @Override
    public IPage<DataSourceEntity> page(BaseQO query) {
        IPage<DataSourceEntity> page = baseMapper.selectPage(
            getPage(query),
            getWrapper(query)
        );
        return page;
    }

    @Override
    public List<DataSourceEntity> getList() {
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    protected IPage<DataSourceEntity> getPage(BaseQO query) {
        Page<DataSourceEntity> page = new Page(query.getPageNum(), query.getPageSize());
        page.addOrder(OrderItem.desc("id"));
        return page;
    }

    protected QueryWrapper<DataSourceEntity> getWrapper(BaseQO query){
        QueryWrapper<DataSourceEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.isNotEmpty(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.isNotEmpty(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.isNotEmpty(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.isNotEmpty(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.isNotEmpty(query.getDbType()), "db_type", query.getDbType());
        return wrapper;
    }

}