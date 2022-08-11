package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysOauthDO;
import io.laokou.admin.interfaces.qo.SysOauthQO;
import io.laokou.admin.interfaces.vo.SysOauthVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/11 0011 上午 9:52
 */
@Mapper
@Repository
public interface SysOauthMapper extends BaseMapper<SysOauthDO> {

    void deleteOauth(@Param("id") Long id);

    SysOauthVO getOauthById(@Param("id") Long id);

    IPage<SysOauthVO> getOauthList(IPage<SysOauthVO> page, @Param("qo") SysOauthQO qo);

}
