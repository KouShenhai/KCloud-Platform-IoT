package io.laokou.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.generator.entity.TableInfoEntity;
import io.laokou.generator.qo.BaseQO;

/**
 * 表
 *
 */
public interface TableInfoService extends IService<TableInfoEntity> {

    IPage<TableInfoEntity> page(BaseQO qo);

    TableInfoEntity getByTableName(String tableName);

    void deleteByTableName(String tableName);

    void deleteBatchIds(Long[] ids);
}