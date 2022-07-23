package io.laokou.admin.domain.wx.repository.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.admin.domain.wx.entity.WXMpMenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 公众号自定义菜单
*
* @author Kou Shenhai
*/
@Mapper
public interface WXMpMenuMapper extends BaseMapper<WXMpMenuDO> {
	
}