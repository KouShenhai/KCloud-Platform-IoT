package io.laokou.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.generator.dao.TableFieldDao;
import io.laokou.generator.entity.TableFieldEntity;
import io.laokou.generator.service.TableFieldService;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * 表
 *

 */
@Service
public class TableFieldServiceImpl extends ServiceImpl<TableFieldDao, TableFieldEntity> implements TableFieldService {

    @Override
    public List<TableFieldEntity> getByTableName(String tableName) {
        return baseMapper.getByTableName(tableName);
    }

    @Override
    public void deleteByTableName(String tableName) {
        baseMapper.deleteByTableName(tableName);
    }

    @Override
    public void deleteBatchTableIds(Long[] tableIds) {
        baseMapper.deleteBatchTableIds(tableIds);
    }

}