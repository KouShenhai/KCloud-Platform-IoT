package org.laokou.common.mybatisplus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface I18nMessageMapper extends CrudMapper<Long, Integer, I18nMessageDO> {

}
