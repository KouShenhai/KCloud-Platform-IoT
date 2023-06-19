package org.laokou.generate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.generate.vo.ColumnVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ColumnMapper {

	List<ColumnVO> getColumns(@Param("tableName") String tableName);

}
