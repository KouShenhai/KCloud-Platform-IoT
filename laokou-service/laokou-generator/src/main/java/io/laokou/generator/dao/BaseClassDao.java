package io.laokou.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.generator.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 *
 */
@Mapper
public interface BaseClassDao extends BaseMapper<BaseClassEntity> {
	
}