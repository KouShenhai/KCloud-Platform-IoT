package io.laokou.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.generator.entity.BaseClassEntity;
import io.laokou.generator.qo.BaseQO;

import java.util.List;

/**
 * 基类管理
 *

 */
public interface BaseClassService extends IService<BaseClassEntity> {

    IPage<BaseClassEntity> page(BaseQO qo);

    List<BaseClassEntity> getList();
}