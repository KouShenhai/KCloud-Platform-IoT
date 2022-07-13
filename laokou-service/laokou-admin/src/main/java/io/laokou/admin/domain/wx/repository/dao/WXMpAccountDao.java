package io.laokou.admin.domain.wx.repository.dao;

import io.laokou.admin.domain.wx.entity.WXMpAccountDO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
* 公众号账号管理
*
* @author Kou Shenhai
*/
@Mapper
public interface WXMpAccountDao extends BaseDao<WXMpAccountDO> {
	
}