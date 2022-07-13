package io.laokou.admin.domain.wx.repository.dao;


import io.laokou.admin.domain.wx.entity.WXMpMenuDO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 公众号自定义菜单
*
* @author Kou Shenhai
*/
@Mapper
public interface WXMpMenuDao extends BaseDao<WXMpMenuDO> {
	
}