package io.laokou.auth.domain.zfb.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.auth.domain.zfb.entity.ZfbUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 */
@Repository
@Mapper
public interface ZfbUserMapper extends BaseMapper<ZfbUserDO> {
}
