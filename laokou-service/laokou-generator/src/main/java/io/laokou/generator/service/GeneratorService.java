package io.laokou.generator.service;
import io.laokou.generator.config.DataSourceInfo;
import io.laokou.generator.entity.TableFieldEntity;
import io.laokou.generator.entity.TableInfoEntity;

import java.util.List;
/**
 * 代码生成
 *
 */
public interface GeneratorService {

    DataSourceInfo getDataSourceInfo(Long datasourceId);

    void datasourceTable(TableInfoEntity tableInfo);

    void updateTableField(Long tableId, List<TableFieldEntity> tableFieldList);

    void generatorCode(TableInfoEntity tableInfo) throws Exception;
}
