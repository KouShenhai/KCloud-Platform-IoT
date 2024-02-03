package org.laokou.sys.mapper;

import org.laokou.sys.entity.SysDeptDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import org.laokou.sys.vo.*;
import org.laokou.sys.qo.*;
import org.apache.ibatis.session.ResultHandler;
/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@Repository
@Mapper
public interface SysDeptMapper extends BatchMapper<SysDeptDO> {
     /**
     * 分页查询部门
     * @param pageQuery
     * @param qo
     * @return
     */
     IPage<SysDeptVO> queryPageList(IPage<SysDeptVO> pageQuery, @Param("qo") SysDeptQo qo);

     /**
     * 查询导出部门
     * @param qo
     * @param handler
     */
     void resultList(@Param("qo") SysDeptQo qo, ResultHandler<SysDeptVO> handler);
}
