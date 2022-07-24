package io.laokou.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.generator.entity.FieldTypeEntity;
import io.laokou.generator.qo.BaseQO;

import java.util.Map;
import java.util.Set;

/**
 * 字段类型管理
 *

 */
public interface FieldTypeService extends IService<FieldTypeEntity> {
    IPage<FieldTypeEntity> page(BaseQO qo);

    Map<String, FieldTypeEntity> getMap();

    /**
     * 根据tableId，获取包列表
     */
    Set<String> getPackageListByTableId(Long tableId);

    Set<String> getList();
}