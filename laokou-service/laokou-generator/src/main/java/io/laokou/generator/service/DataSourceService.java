package io.laokou.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.generator.entity.DataSourceEntity;
import io.laokou.generator.qo.BaseQO;

import java.util.List;

/**
 * 数据源管理
 *

 */
public interface DataSourceService extends IService<DataSourceEntity> {

    IPage<DataSourceEntity> page(BaseQO qo);

    List<DataSourceEntity> getList();
}