package org.laokou.sys.mapper;

import org.laokou.sys.entity.SysDictDO;
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
 * 字典 Mapper 接口
 * </p>
 *
 * @author laokou
 * @since 2024-02-01
 */
@Repository
@Mapper
public interface SysDictMapper extends BatchMapper<SysDictDO> {
     /**
     * 分页查询字典
     * @param pageQuery
     * @param qo
     * @return
     */
     IPage<SysDictVO> queryPageList(IPage<SysDictVO> pageQuery, @Param("qo") SysDictQo qo);

     /**
     * 查询导出字典
     * @param qo
     * @param handler
     */
     void resultList(@Param("qo") SysDictQo qo, ResultHandler<SysDictVO> handler);
}
