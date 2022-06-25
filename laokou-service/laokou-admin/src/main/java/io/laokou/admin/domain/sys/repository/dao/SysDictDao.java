package io.laokou.admin.domain.sys.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/6/23 0023 上午 10:22
 */
@Repository
@Mapper
public interface SysDictDao extends BaseDao<SysDictDO> {

    List<DictVO> getDictList(@Param("qo") DictQO qo);

    IPage<DictVO> getDictList(IPage<DictVO> page, @Param("qo") DictQO qo);

    DictVO getDictById(@Param("id") Long id);

    void deleteDict(@Param("id") Long id);

}
