package io.laokou.admin.domain.wx.repository.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.wx.entity.WXMpMenuDO;
import io.laokou.admin.domain.wx.repository.dao.WXMpMenuDao;
import io.laokou.admin.domain.wx.repository.service.WXMpMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 公众号自定义菜单
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WXMpMenuServiceImpl extends ServiceImpl<WXMpMenuDao, WXMpMenuDO> implements WXMpMenuService {

//    @Override
//    public QueryWrapper<WXMpMenuDO> getWrapper(Map<String, Object> params){
//        QueryWrapper<WXMpMenuDO> wrapper = new QueryWrapper<>();
//
//        return wrapper;
//    }
//
//    @Override
//    public WXMpMenuDTO getByAppId(String appId) {
//        WXMpMenuDO entity = baseDao.selectOne(new QueryWrapper<WXMpMenuDO>().eq("app_id", appId));
//        return ConvertUtil.sourceToTarget(entity, WXMpMenuDTO.class);
//    }
//
//    @Override
//    public void deleteByAppId(String appId) {
//        baseDao.delete(new QueryWrapper<WXMpMenuDO>().eq("app_id", appId));
//    }
}