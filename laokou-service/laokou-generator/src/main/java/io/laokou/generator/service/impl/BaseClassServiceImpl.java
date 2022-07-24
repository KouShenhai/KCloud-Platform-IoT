
package io.laokou.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.generator.dao.BaseClassDao;
import io.laokou.generator.entity.BaseClassEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.BaseClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基类管理
 *

 */
@Service
public class BaseClassServiceImpl extends ServiceImpl<BaseClassDao, BaseClassEntity> implements BaseClassService {

    @Override
    public IPage<BaseClassEntity> page(BaseQO query) {
        IPage<BaseClassEntity> page = baseMapper.selectPage(
            getPage(query), getWrapper(query)
        );

        return page;
    }

    @Override
    public List<BaseClassEntity> getList() {
        return baseMapper.selectList(null);
    }

    protected IPage<BaseClassEntity> getPage(BaseQO query) {
        Page<BaseClassEntity> page = new Page(query.getPageNum(), query.getPageSize());
        page.addOrder(OrderItem.desc("id"));
        return page;
    }

    protected QueryWrapper<BaseClassEntity> getWrapper(BaseQO query){
        QueryWrapper<BaseClassEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.isNotEmpty(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.isNotEmpty(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.isNotEmpty(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.isNotEmpty(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.isNotEmpty(query.getDbType()), "db_type", query.getDbType());
        return wrapper;
    }

}