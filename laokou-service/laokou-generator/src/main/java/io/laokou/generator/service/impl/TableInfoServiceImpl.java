package io.laokou.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.generator.dao.TableInfoDao;
import io.laokou.generator.entity.BaseClassEntity;
import io.laokou.generator.entity.TableInfoEntity;
import io.laokou.generator.qo.BaseQO;
import io.laokou.generator.service.TableFieldService;
import io.laokou.generator.service.TableInfoService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;


/**
 * 表
 *

 */
@Service
@AllArgsConstructor
public class TableInfoServiceImpl extends ServiceImpl<TableInfoDao, TableInfoEntity> implements TableInfoService {
    private final TableFieldService tableFieldService;

    @Override
    public IPage<TableInfoEntity> page(BaseQO qo) {
        IPage<TableInfoEntity> page = baseMapper.selectPage(
            getPage(qo),
            getWrapper(qo)
        );
        return page;
    }

    protected IPage<TableInfoEntity> getPage(BaseQO query) {
        Page<TableInfoEntity> page = new Page(query.getPageNum(), query.getPageSize());
        page.addOrder(OrderItem.desc("id"));
        return page;
    }

    protected QueryWrapper<TableInfoEntity> getWrapper(BaseQO query){
        QueryWrapper<TableInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.isNotEmpty(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.isNotEmpty(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.isNotEmpty(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.isNotEmpty(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.isNotEmpty(query.getDbType()), "db_type", query.getDbType());
        return wrapper;
    }

    @Override
    public TableInfoEntity getByTableName(String tableName) {
        return baseMapper.getByTableName(tableName);
    }

    @Override
    public void deleteByTableName(String tableName) {
        baseMapper.deleteByTableName(tableName);
    }

    @Override
    public void deleteBatchIds(Long[] ids) {
        //删除表
        baseMapper.deleteBatchIds(Arrays.asList(ids));

        //删除列
        tableFieldService.deleteBatchTableIds(ids);
    }



}