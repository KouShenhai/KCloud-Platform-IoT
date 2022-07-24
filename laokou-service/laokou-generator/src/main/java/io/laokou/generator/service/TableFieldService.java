package io.laokou.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.generator.entity.TableFieldEntity;

import java.util.List;

/**
 * 列
 *

 */
public interface TableFieldService extends IService<TableFieldEntity> {

    List<TableFieldEntity> getByTableName(String tableName);

    void deleteByTableName(String tableName);

    void deleteBatchTableIds(Long[] tableIds);
}