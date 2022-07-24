package io.laokou.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.generator.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 *

 */
@Mapper
public interface DataSourceDao extends BaseMapper<DataSourceEntity> {
	
}